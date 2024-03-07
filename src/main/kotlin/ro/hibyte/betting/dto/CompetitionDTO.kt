package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Status
import java.time.Instant

data class CompetitionDTO(
    var id: Long? = null,
    var name: String = "",
    var description: String = "",
    var creator: UserProfileDTO,
    var users: List<UserProfileDTO> = emptyList(),
    var userGroups: List<String> = emptyList(),
    var userProfiles: List<String> = emptyList(),
    var events: List<EventDTO> = emptyList(),
    var created : Instant = Instant.now(),
    var lastModified : Instant = Instant.now(),
    var status: Status = Status.DRAFT,
)
