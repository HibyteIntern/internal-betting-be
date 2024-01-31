package ro.hibyte.betting.demodata

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.CompleteBetTypeDto
import ro.hibyte.betting.dto.EventDTO
import ro.hibyte.betting.dto.UserProfileDTO

fun main() {
    val yamlReader = YamlReader()

    val restTemplate = RestTemplate()
    createUsers(restTemplate, yamlReader)

    createEvents(restTemplate, yamlReader)

    val allUsers: Array<UserProfileDTO> =
        restTemplate.getForObject("http://localhost:8080/api/v1/user-profile", Array<UserProfileDTO>::class.java)!!
    val betTypes: Array<CompleteBetTypeDto> =
        restTemplate.getForObject("http://localhost:8080/api/v1/bet-types", Array<CompleteBetTypeDto>::class.java)!!

    createRandomBets(allUsers, betTypes)

    val events: Array<EventDTO> =
        restTemplate.getForObject("http://localhost:8080/api/v1/events", Array<EventDTO>::class.java)!!

    findRandomOutcomes(restTemplate, events, betTypes)
}

fun findRandomOutcomes(restTemplate: RestTemplate, events: Array<EventDTO>, betTypes: Array<CompleteBetTypeDto>) {
    val betTypeOutcomes: Map<Long?, String> = betTypes.associate { it.id to (it.multipleChoiceOptions?.random() ?: "") }
    val eventNo = events.map { event ->
        restTemplate.put(
            "http://localhost:8080/api/v1/events/outcome/${event.eventId}",
            HttpEntity(betTypeOutcomes),
        )
    }.count()
    println("Created $eventNo outcomes")
}

fun createRandomBets(allUsers: Array<UserProfileDTO>, betTypes: Array<CompleteBetTypeDto>): Any {
    val restTemplate = RestTemplate()
    val random = java.util.Random()
    val bets = allUsers.flatMap { user ->
        // get a random number of bets between 5 and 10
        val numberOfBets = random.nextInt(5) + 5
        (0..numberOfBets).map {
            val betType = betTypes.random()
            val betValue = betType.multipleChoiceOptions?.random()!!
            BetDTO(
                value = betValue,
                amount = random.nextInt(5) + 5,
                betType = betType.id,
                user = user.userId
            )
        }
    }
    val response: Array<BetDTO> = restTemplate.postForEntity(
        "http://localhost:8080/api/v1/bets/many",
        HttpEntity(bets),
        Array<BetDTO>::class.java
    ).body!!
    println("Created ${response.size} bets")
    return response
}

fun createEvents(restTemplate: RestTemplate, yamlReader: YamlReader) {
    val events = yamlReader.loadYamlAsList<EventDTO>("events.yml")

    val response: Array<EventDTO> = restTemplate.postForEntity(
        "http://localhost:8080/api/v1/events/many",
        HttpEntity(events),
        Array<EventDTO>::class.java
    ).body!!
    println("Created ${response.size} events")
}

private fun createUsers(
    restTemplate: RestTemplate,
    yamlReader: YamlReader
) {
    val userProfiles = yamlReader.loadYamlAsList<UserProfileDTO>("users.yml")

    val response: Array<UserProfileDTO> = restTemplate.postForEntity(
        "http://localhost:8080/api/v1/user-profile/many",
        HttpEntity(userProfiles),
        Array<UserProfileDTO>::class.java
    ).body!!
    println("Created ${response.size} user profiles")
}

class YamlReader {
    inline fun <reified T> loadYamlAsList(filePath: String): List<T> {
        YamlReader::class.java.classLoader.getResourceAsStream("data/$filePath").use { inputStream ->
            val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
            return mapper.readValue(inputStream, Array<T>::class.java).toList()
        }
    }
}
