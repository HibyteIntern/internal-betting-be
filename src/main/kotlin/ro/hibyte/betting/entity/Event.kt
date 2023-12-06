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
    @OneToMany
    @JoinColumn(name = "eventId")
    var users:List<UserProfile> = emptyList(),
    @ElementCollection
    var userGroups : List<String> = emptyList(),
    @ElementCollection
    var userProfiles : List<Long?> = emptyList(),
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
        users:List<UserProfile>,
        userGroups: List<String>,
        userProfiles: List<Long?>,
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
        users,
        userGroups,
        userProfiles,
        created,
        lastModified,
        startsAt,
        endsAt,
        status
    )
}