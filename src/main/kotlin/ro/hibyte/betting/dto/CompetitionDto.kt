package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Status
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
    var status: Status = Status.Draft
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
        status = competition.status
    )
}