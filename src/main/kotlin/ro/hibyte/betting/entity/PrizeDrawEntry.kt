package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
data class PrizeDrawEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @ManyToOne
    var user: UserProfile,
    var amount: Number,
    @ManyToOne
    @JsonBackReference
    var prizeDraw: PrizeDraw
)
