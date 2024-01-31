package ro.hibyte.betting.entity

import jakarta.persistence.*


@Entity
data class Leaderboard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "leaderboard_events",
        joinColumns = [JoinColumn(name = "leaderboard_id")],
        inverseJoinColumns = [JoinColumn(name = "event_id")]
    )
    var events: Set<Event> = HashSet(),

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "leaderboard_user_profiles",
        joinColumns = [JoinColumn(name = "leaderboard_id")],
        inverseJoinColumns = [JoinColumn(name = "user_profile_id")]
    )
    var userProfiles: Set<UserProfile> = HashSet()
)
