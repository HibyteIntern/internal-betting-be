package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.entity.UserProfile
import java.sql.Timestamp
import java.time.Instant

data class CompetitionDto(
    var id: Long? = null,
    var name: String = "",
    var description: String = "",
    var creator: String = "",
    var users: Set<UserProfile> = emptySet(),
    var userGroups: List<String> = emptyList(),
    var userProfiles: List<String> = emptyList(),
    var events: List<Event> = emptyList(),
    var created : Instant = Instant.now(),
    var lastModified : Instant = Instant.now(),
    var status: Status = Status.DRAFT,
)
