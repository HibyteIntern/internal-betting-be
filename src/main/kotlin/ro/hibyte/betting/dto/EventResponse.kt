package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Status
import java.time.Instant

data class EventResponse(
    var eventId: Long,
    var name: String,
    var description: String ,
    var creator:String ,
    var tags: List<String>,
    var userGroups : List<String> ,
    var userProfiles : List<String>,
    var completeBetTypeDtoList: List<CompleteBetTypeDto>,
    var created : Instant,
    var lastModified : Instant,
    var startsAt : Instant,
    var endsAt : Instant,
    var status: Status
) {
}