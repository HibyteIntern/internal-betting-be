package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Status
import java.time.Instant

data class EventDTO(
    var eventId: Long? =null,
    var name: String?=null,
    var description: String?=null,
    var creator: String? = null,
    var tags: List<String>? = null,
    var combinedUserProfileIds: Set<Long?>? = null,
    var userGroupIds : Set<Long>? = null,
    var userProfileIds : Set<Long?>?=null,
    var betTypeDtoList: List<BetTypeDTO>?=null,
    var bets:List<CompleteBetDTO>?=null,
    var created : Instant?=null,
    var lastModified : Instant?=null,
    var startsAt : Instant?=null,
    var endsAt : Instant?=null,
    var status: Status?=null,
    var selectedTemplate: String?=null
)
