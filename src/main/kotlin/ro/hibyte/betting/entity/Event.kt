package ro.hibyte.betting.entity

import com.fasterxml.jackson.databind.BeanDescription
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var eventId: Long = 0,
    var name: String = "",
    var description: String = "",
    var creator:String = "",
    var template:String = "",
    @ElementCollection
    var tags: List<String> = emptyList(),
//    bet types
//    bets
//    users
    @ElementCollection
    var userGroups : List<String> = emptyList(),
    @ElementCollection
    var userProfiles : List<String> = emptyList(),
    var created : Timestamp = Timestamp(System.currentTimeMillis()),
    var lastModified :Timestamp = Timestamp(System.currentTimeMillis()),
    var startsAt :Timestamp = Timestamp(System.currentTimeMillis()),
    var endsAt :Timestamp = Timestamp(System.currentTimeMillis()),
    var status: Status = Status.Draft
) {
    constructor(
        name: String,
        description: String,
        creator: String,
        template: String,
        tags: List<String>,
        userGroups: List<String>,
        userProfiles: List<String>,
        created: Timestamp,
        lastModified: Timestamp,
        startsAt: Timestamp,
        endsAt: Timestamp,
        status: Status
    ) : this(
        0,
        name,
        description,
        creator,
        template,
        tags,
        userGroups,
        userProfiles,
        created,
        lastModified,
        startsAt,
        endsAt,
        status
    )
}