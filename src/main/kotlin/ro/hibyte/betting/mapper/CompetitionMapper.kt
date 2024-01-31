package ro.hibyte.betting.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.UserGroupRepository
import ro.hibyte.betting.repository.UserProfileRepository
import java.sql.Timestamp

@Service
class CompetitionMapper {
    @Autowired
    private lateinit var eventRepository: EventRepository
    @Autowired
    private lateinit var userProfileRepository: UserProfileRepository
    @Autowired
    private lateinit var userGroupRepository: UserGroupRepository

    fun getEventsFromNames(eventNames: List<String>): List<Event> = eventNames.map { name -> eventRepository.findByName(name).first() }.toList()

    fun getUserProfilesFromUserGroupNamesAndUsernames(userGroupNames: List<String>, usernames: List<String>): List<UserProfile> {
        val userGroupsFromNames = userGroupNames.map{ groupName -> userGroupRepository.findByGroupName(groupName) }.toList()
        val userGroupSet = userGroupsFromNames.map { group -> group.users }
        val usersFromGroups = userGroupSet.flatMap { it.orEmpty() }.toSet().toList()
        val userProfilesFromGivenUsernames = usernames.map{ username -> userProfileRepository.findByUsername(username) }.toList()

        return listOf(usersFromGroups, userProfilesFromGivenUsernames).flatten().toSet().toList()
    }

        fun mapCompetitionRequestToCompetition(competitionRequest: CompetitionRequest): Competition =
            Competition(
                name = competitionRequest.name,
                description = competitionRequest.description,
                creator = "",
                users = getUserProfilesFromUserGroupNamesAndUsernames(competitionRequest.userGroups, competitionRequest.userProfiles),
                userGroups = competitionRequest.userGroups,
                userProfiles = competitionRequest.userProfiles,
                events = getEventsFromNames(competitionRequest.events),
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
            users = getUserProfilesFromUserGroupNamesAndUsernames(competitionRequest.userGroups, competitionRequest.userProfiles),
            userGroups = competitionRequest.userGroups,
            userProfiles = competitionRequest.userProfiles,
            events = getEventsFromNames(competitionRequest.events),
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
