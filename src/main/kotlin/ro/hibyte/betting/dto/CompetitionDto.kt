package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Status
import java.sql.Timestamp
import java.time.Instant

class CompetitionDto(
    var id: Long? = null,
    var name: String? = null,
    var creator: String? = null,
    // TODO: users
    var userGroups: List<String>? = null,
    var userProfiles: List<String>? = null,
    // TODO: events
    var created : Instant? = null,
    var lastModified : Instant? = null,
    var status: String? = null
) {
    constructor(competition: Competition) : this(
        id = competition.id,
        name = competition.name,
        creator = competition.creator,
        // TODO: users
        userGroups = competition.userGroups,
        userProfiles = competition.userProfiles,
        // TODO: events
        created = competition.created?.toInstant(),
        lastModified = competition.lastModified?.toInstant(),
        status = when(competition.status) {
            Status.Draft -> "Draft"
            Status.Open -> "Open"
            Status.Closed -> "Closed"
            else -> null
        }
    )
}