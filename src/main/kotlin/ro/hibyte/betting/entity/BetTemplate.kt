package ro.hibyte.betting.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class BetTemplate (
    @Id
    @GeneratedValue
    val id: Long,
    var name: String
) {
    fun update(betTemplate: BetTemplate) {
        name = betTemplate.name
    }
}