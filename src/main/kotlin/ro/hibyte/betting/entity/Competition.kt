package ro.hibyte.betting.entity

import jakarta.persistence.*
import org.springframework.beans.factory.annotation.Autowired
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.service.CompetitionService
import java.sql.Timestamp

@Entity
data class Competition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var competitionId: Long = 0,
    var name: String = "",
    var description: String = "",
    var creator: String = "",
    @ManyToMany
    @JoinTable(
        name = "competitions_user",
        joinColumns = [JoinColumn(name = "competition_id")],
        inverseJoinColumns = [JoinColumn(name = "user_profile_id")]
    )
    var users: List<UserProfile> = emptyList(),
    @ElementCollection
    var userGroups: List<String> = emptyList(),
    @ElementCollection
    var userProfiles: List<String> = emptyList(),
    @ManyToMany
    @JoinTable(
        name = "competitions_events",
        joinColumns = [JoinColumn(name = "competition_id")],
        inverseJoinColumns = [JoinColumn(name = "event_id")]
    )
    var events: List<Event> = emptyList(),
    @Temporal(TemporalType.TIMESTAMP)
    var created : Timestamp = Timestamp(System.currentTimeMillis()),
    @Temporal(TemporalType.TIMESTAMP)
    var lastModified : Timestamp = Timestamp(System.currentTimeMillis()),
    var status: Status = Status.DRAFT
)
