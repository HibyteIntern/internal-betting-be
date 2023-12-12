package ro.hibyte.betting.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.exceptions.types.*
import ro.hibyte.betting.repository.CompetitionRepository
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.UserProfileRepository
import java.time.Instant

@Service
class CompetitionService {


    companion object {
        @Autowired
        private lateinit var competitionRepository : CompetitionRepository
        @Autowired
        private lateinit var eventRepository: EventRepository
        @Autowired
        private lateinit var userProfileRepository: UserProfileRepository
        fun getEventsFromIds(eventIDs: List<Long>): List<Event> = eventIDs.map { id -> eventRepository.findById(id).orElseThrow{ EntityNotFoundException("Event", id) } }.toList()

        fun getUserProfilesFromIds(userIDs: List<Long>): Set<UserProfile> = userIDs.map { id -> userProfileRepository.findById(id).orElseThrow { EntityNotFoundException("User Profile", id) } }.toSet()
    }

    fun getAll(): List<CompetitionDto> {
        val competitions = competitionRepository.findAll()

        return competitions.map { CompetitionDto(it) }
    }

    fun getOne(id: Long): Competition = competitionRepository.findById(id).orElseThrow{ EntityNotFoundException("Competition", id) }

    fun create(competitionRequest: CompetitionRequest): Competition = competitionRepository.save(Competition(CompetitionDto(competitionRequest)))


    fun update(id: Long, competitionRequest: CompetitionRequest): Competition {
        val competition = competitionRepository.findById(id).orElseThrow{ EntityNotFoundException("Competition", id) }

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
