package ro.hibyte.betting.dto

import jakarta.persistence.ElementCollection
import ro.hibyte.betting.entity.Status
import java.time.Instant

data class EventResponse(
    var eventId: Long,
    var name: String,
    var description: String ,
    var creator:String ,
    @ElementCollection
    var tags: List<String>,
    @ElementCollection
    var userGroups : List<String> ,
    @ElementCollection
    var userProfiles : List<String>,
    var created : Instant,
    var lastModified : Instant,
    var startsAt : Instant,
    var endsAt : Instant,
    var status: Status
) {
}