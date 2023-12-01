package ro.hibyte.betting.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class BetType (
    @Id
    @GeneratedValue
    var id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_template_id")
    var betTemplate: BetTemplate

    //odds will be added later
)