package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import ro.hibyte.betting.dto.PrizeDrawRequest
import java.sql.Timestamp

@Entity
data class PrizeDraw(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String,
    @Column(length = 2048)
    var description: String,
    var status: Status,
    var createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    var endsAt: Timestamp,
    @Column(length = 1024)
    var prizeDescription: String,
    var type: DrawType,
    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    var winner: UserProfile?,

    @OneToMany(mappedBy = "prizeDraw", cascade = [CascadeType.ALL])
    @JsonManagedReference
    var entries: List<PrizeDrawEntry> = emptyList(),
) {
    fun update(prizeDrawRequest: PrizeDrawRequest) {
        this.title = prizeDrawRequest.title
        this.description = prizeDrawRequest.description
        this.endsAt = Timestamp.from(prizeDrawRequest.endsAt)
        this.prizeDescription = prizeDrawRequest.prizeDescription
    }
}
