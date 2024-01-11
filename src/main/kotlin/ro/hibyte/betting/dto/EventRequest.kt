package ro.hibyte.betting.dto

import jakarta.persistence.ElementCollection
import ro.hibyte.betting.entity.Status
import java.sql.Timestamp
import java.time.Instant

data class EventRequest(
//    to add user and bet related fields when implemented
    var name: String ,
    var description: String ,
    var completeBetTypeDtoList: List<CompleteBetTypeDto>,
    var userProfileIdList: List<Long>,
    var startsAt : Instant ,
    var endsAt : Instant,
    var status: Status
) {
}