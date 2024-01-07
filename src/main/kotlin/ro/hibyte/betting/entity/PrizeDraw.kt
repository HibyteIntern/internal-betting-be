package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.sql.Timestamp

@Entity
data class PrizeDraw(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String,
    var description: String,
    var status: Status,
    var createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    var endsAt: Timestamp,
    var prizeDescription: String,
    var type: DrawType,
    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    var winner: UserProfile?,

    @OneToMany(mappedBy = "prizeDraw", cascade = [CascadeType.ALL])
    @JsonManagedReference
    var entries: List<PrizeDrawEntry> = emptyList(),
)
