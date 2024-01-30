package ro.hibyte.betting.demodata

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.http.HttpEntity
import org.springframework.web.client.RestTemplate
import ro.hibyte.betting.dto.UserProfileDTO

fun main() {
    val yamlReader = YamlReader()
    val userProfiles = yamlReader.loadYamlAsList<UserProfileDTO>("users.yml")

    val restTemplate = RestTemplate()
    val response: Array<UserProfileDTO> = restTemplate.postForEntity("http://localhost:8080/api/v1/user-profile/many", HttpEntity(userProfiles), Array<UserProfileDTO>::class.java).body!!
    println(response)
}

class YamlReader {
    inline fun <reified T> loadYamlAsList(filePath: String): List<T> {
        YamlReader::class.java.classLoader.getResourceAsStream("data/$filePath").use { inputStream ->
            val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
            return mapper.readValue(inputStream, Array<T>::class.java).toList()
        }
    }
}
