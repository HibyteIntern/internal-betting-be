package ro.hibyte.betting.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.CompetitionRepository
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.UserProfileRepository
import java.sql.Timestamp
import java.time.Instant

@Service
class CompetitionMapper {
    @Autowired
    private lateinit var eventRepository: EventRepository
    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository

    fun getEventsFromIds(eventIDs: List<Long>): List<Event> =
        eventIDs.map {
            id -> eventRepository.findById(id).orElseThrow{ EntityNotFoundException("Event", id) }
        }.toList()

    fun getUserProfilesFromIds(userIDs: List<Long>): Set<UserProfile> =
        userIDs.map {
            id -> userProfileRepository.findById(id).orElseThrow { EntityNotFoundException("User Profile", id) }
        }.toSet()

    fun mapCompetitionRequestToCompetition(competitionRequest: CompetitionRequest): Competition =
        Competition(
            name = competitionRequest.name,
            description = competitionRequest.description,
            creator = "",
            users = getUserProfilesFromIds(competitionRequest.users),
            userGroups = competitionRequest.userGroups,
            userProfiles = competitionRequest.userProfiles,
            events = getEventsFromIds(competitionRequest.events),
            created = Timestamp(System.currentTimeMillis()),
            lastModified = Timestamp(System.currentTimeMillis()),
            status = competitionRequest.status,
        )

    fun mapCompetitionRequestToCompetition(competitionRequest: CompetitionRequest, initialCompetition: Competition): Competition =
        Competition(
            competitionId = initialCompetition.competitionId,
            name = competitionRequest.name,
            description = competitionRequest.description,
            creator = "",
            users = getUserProfilesFromIds(competitionRequest.users),
            userGroups = competitionRequest.userGroups,
            userProfiles = competitionRequest.userProfiles,
            events = getEventsFromIds(competitionRequest.events),
            created = initialCompetition.created,
            lastModified = Timestamp(System.currentTimeMillis()),
            status = competitionRequest.status,
        )

    fun mapCompetitionToCompetitionDto(competition: Competition): CompetitionDto =
        CompetitionDto(
            id = competition.competitionId,
            name = competition.name,
            description = competition.description,
            creator = competition.creator,
            users = competition.users,
            userGroups = competition.userGroups,
            userProfiles = competition.userProfiles,
            events = competition.events,
            created = competition.created.toInstant(),
            lastModified = competition.lastModified.toInstant(),
            status = competition.status,
        )
}
