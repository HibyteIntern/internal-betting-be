package ro.hibyte.betting.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.exceptions.types.*
import ro.hibyte.betting.repository.CompetitionRepository
import java.time.Instant

@Service
class CompetitionService {
    @Autowired
    private lateinit var competitionRepository : CompetitionRepository
    @Autowired
    private lateinit var eventRepository: EventRepository
    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository

    fun getEventsFromIds(eventIDs: List<Long>): List<Event> {
        val events: MutableList<Event> = ArrayList()

        eventIDs.forEach{ id -> events.add(eventRepository.findById(id).orElseThrow{ EventNotFoundException(id) }) }

        return events
    }

    fun getUserProfilesFromIds(userIDs: List<Long>): Set<UserProfile> {
        val userProfiles: MutableSet<UserProfile> = mutableSetOf()

        userIDs.forEach{ id -> userProfiles.add(userProfileRepository.findById(id).orElseThrow { UserProfileNotFoundException(id) }) }

        return userProfiles
    }

    fun getAll(): List<CompetitionDto> {
        val competitions = competitionRepository.findAll()

        return competitions.map { CompetitionDto(it) }
    }

    fun getOne(id: Long): Competition = competitionRepository.findById(id).orElseThrow{ CompetitionNotFoundException(id) }

    fun create(dto: CompetitionRequest): Competition {
        val competition = CompetitionDto()

        competition.name = dto.name
        competition.creator = ""
        competition.users = getUserProfilesFromIds(dto.users)
        competition.userGroups = dto.userGroups
        competition.userProfiles = dto.userProfiles
        competition.events = getEventsFromIds(dto.events)
        competition.created = Instant.now()
        competition.lastModified = Instant.now()
        competition.status = dto.status

        return competitionRepository.save(Competition(competition))
    }


    fun update(id: Long, competitionRequest: CompetitionRequest): Competition {
        val competition = competitionRepository.findById(id).orElseThrow{ CompetitionNotFoundException(id) }

        val newCompetitionDto = CompetitionDto(competition)

        newCompetitionDto.name = competitionRequest.name
        competition.users = getUserProfilesFromIds(competitionRequest.users)
        newCompetitionDto.userGroups = competitionRequest.userGroups
        newCompetitionDto.userProfiles = competitionRequest.userProfiles
        newCompetitionDto.events = getEventsFromIds(competitionRequest.events)
        newCompetitionDto.lastModified = Instant.now()
        newCompetitionDto.status = competitionRequest.status

        val newCompetition = Competition(newCompetitionDto)
        newCompetition.competitionId = id

        return competitionRepository.save(newCompetition)
    }

    fun delete(id: Long) = competitionRepository.deleteById(id)
}