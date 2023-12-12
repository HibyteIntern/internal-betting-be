package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.service.CompetitionService
import java.time.Instant

data class CompetitionDto(
    var id: Long? = null,
    var name: String = "",
    var creator: String = "",
    var users: Set<UserProfile> = emptySet(),
    var userGroups: List<String> = emptyList(),
    var userProfiles: List<String> = emptyList(),
    var events: List<Event> = emptyList(),
    var created : Instant = Instant.now(),
    var lastModified : Instant = Instant.now(),
    var status: Status = Status.DRAFT,
) {
    constructor(competition: Competition) : this(
        id = competition.competitionId,
        name = competition.name,
        creator = competition.creator,
        users = competition.users,
        userGroups = competition.userGroups,
        userProfiles = competition.userProfiles,
        events = competition.events,
        created = competition.created.toInstant(),
        lastModified = competition.lastModified.toInstant(),
        status = competition.status,
    )

    constructor(competitionRequest: CompetitionRequest) : this(
        name = competitionRequest.name,
        creator = "",
        users = CompetitionService.getUserProfilesFromIds(competitionRequest.users),
        userGroups = competitionRequest.userGroups,
        userProfiles = competitionRequest.userProfiles,
        events = CompetitionService.getEventsFromIds(competitionRequest.events),
        created = Instant.now(),
        lastModified = Instant.now(),
        status = competitionRequest.status,
    )

    fun update(competitionRequest: CompetitionRequest) {

    }
}
