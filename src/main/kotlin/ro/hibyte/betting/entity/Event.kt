package ro.hibyte.betting.entity

import com.fasterxml.jackson.databind.BeanDescription
import jakarta.persistence.*
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
    @OneToMany
    @JoinColumn(name  = "eventId")
    var betTypes:List<BetType> = emptyList(),
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
        betTypes: List<BetType>,
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
        betTypes,
        userGroups,
        userProfiles,
        created,
        lastModified,
        startsAt,
        endsAt,
        status
    )
}