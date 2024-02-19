package ro.hibyte.betting.entity

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var eventId: Long = 0,
    var name: String = "",
    var description: String = "",
    var creator: String = "",
    var template: String = "",
    @ElementCollection
    var tags: List<String> = emptyList(),
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "eventId")
    var betTypes: List<BetType> = emptyList(),
    @OneToMany
    @JoinColumn(name = "eventId")
    var userProfiles: Set<UserProfile> = emptySet(),
    @ElementCollection
    var userGroupIds: Set<Long> = emptySet(),
    @ElementCollection
    var userProfileIds: Set<Long?> = emptySet(),
    var created: Timestamp = Timestamp(System.currentTimeMillis()),
    var lastModified: Timestamp = Timestamp(System.currentTimeMillis()),
    var startsAt: Timestamp = Timestamp(System.currentTimeMillis()),
    var endsAt: Timestamp = Timestamp(System.currentTimeMillis()),
    var status: Status = Status.DRAFT
)
