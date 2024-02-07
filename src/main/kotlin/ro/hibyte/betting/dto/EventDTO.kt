package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Status
import java.time.Instant

data class EventDTO(
    var eventId: Long? =null,
    var name: String?=null,
    var description: String?=null,
    var creator:String? = null,
    var tags: List<String>? = null,
    var userProfiles: Set<Long?>? = null,
    var userGroupIds : Set<Long>? = null,
    var userProfileIds : Set<Long?>?=null,
    var completeBetTypeDtoList: List<CompleteBetTypeDto>?=null,
    var bets:List<BetDTO>?=null,
    var created : Instant?=null,
    var lastModified : Instant?=null,
    var startsAt : Instant?=null,
    var endsAt : Instant?=null,
    var status: Status?=null,
    var selectedTemplate: String?=null
)
