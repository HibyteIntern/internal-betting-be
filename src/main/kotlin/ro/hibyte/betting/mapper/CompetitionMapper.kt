package ro.hibyte.betting.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDTO
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.dto.UserProfileDTO
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
    @Autowired
    private lateinit var eventMapper: EventMapper

    fun getEventsFromNames(eventNames: List<String>): List<Event> = eventNames.map { name -> eventRepository.findByName(name).first() }.toList()

    fun getUserProfilesFromUserGroupNamesAndUsernames(userGroupNames: List<String>, usernames: List<String>): List<UserProfile> {
        val uniqueUserGroupNames = userGroupNames.toMutableSet().toMutableList()
        val userGroupsFromNames = uniqueUserGroupNames.map{ groupName -> userGroupRepository.findByGroupName(groupName) }.toList()
        val userGroupSet = userGroupsFromNames.map { group -> group.users }

        val uniqueUsernames = usernames.toMutableSet().toMutableList()
        val usersFromGroups = userGroupSet.flatMap { it.orEmpty() }.toSet().toList()
        val userProfilesFromGivenUsernames = uniqueUsernames.mapNotNull{ username -> userProfileRepository.findByUsername(username) }.toList()

        return listOf(usersFromGroups, userProfilesFromGivenUsernames).flatten().toSet().toList()
    }

    fun mapCompetitionRequestToCompetition(
        competitionRequest: CompetitionRequest,
        creator: UserProfile
    ): Competition = Competition(
            name = competitionRequest.name,
            description = competitionRequest.description,
            creator = creator,
            users = getUserProfilesFromUserGroupNamesAndUsernames(competitionRequest.userGroups, competitionRequest.userProfiles),
            userGroups = competitionRequest.userGroups.toMutableSet().toList(),
            userProfiles = competitionRequest.userProfiles.toMutableSet().toList(),
            events = getEventsFromNames(competitionRequest.events),
            created = Timestamp(System.currentTimeMillis()),
            lastModified = Timestamp(System.currentTimeMillis()),
            status = competitionRequest.status,
        )

    fun mapCompetitionRequestToCompetition(
        competitionRequest: CompetitionRequest,
        initialCompetition: Competition,
        creator: UserProfile
    ): Competition = Competition(
            competitionId = initialCompetition.competitionId,
            name = competitionRequest.name,
            description = competitionRequest.description,
            creator = creator,
            users = getUserProfilesFromUserGroupNamesAndUsernames(competitionRequest.userGroups, competitionRequest.userProfiles),
            userGroups = competitionRequest.userGroups.toMutableSet().toList(),
            userProfiles = competitionRequest.userProfiles.toMutableSet().toList(),
            events = getEventsFromNames(competitionRequest.events),
            created = initialCompetition.created,
            lastModified = Timestamp(System.currentTimeMillis()),
            status = competitionRequest.status,
        )

    fun mapCompetitionToCompetitionDto(competition: Competition): CompetitionDTO =
        CompetitionDTO(
            id = competition.competitionId,
            name = competition.name,
            description = competition.description,
            creator = UserProfileDTO(competition.creator),
            users = competition.users.map { user -> UserProfileDTO(user) },
            userGroups = competition.userGroups,
            userProfiles = competition.userProfiles,
            events = competition.events.map { event -> eventMapper.mapEventToEventResponse(event) },
            created = competition.created.toInstant(),
            lastModified = competition.lastModified.toInstant(),
            status = competition.status,
        )
}
