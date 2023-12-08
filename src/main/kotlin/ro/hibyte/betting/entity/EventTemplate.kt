package ro.hibyte.betting.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany

@Entity
data class EventTemplate(

    @Id
    @GeneratedValue
    var id: Long? = null,

    var name: String,

    @ManyToMany
    @JoinTable(
        name = "event_template_bets",
        joinColumns = [JoinColumn(name = "event_template_id")],
        inverseJoinColumns = [JoinColumn(name = "bet_template_id")]
    )
    var betTemplates: List<BetTemplate> = ArrayList()
) {

    fun update(eventTemplate: EventTemplate) {
        name = eventTemplate.name
        betTemplates = eventTemplate.betTemplates
    }

}