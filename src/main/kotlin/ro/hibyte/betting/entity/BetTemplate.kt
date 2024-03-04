package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.exceptions.types.BadRequestException

@Entity
data class BetTemplate (
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var name: String,

    @ElementCollection
    @CollectionTable(name = "bet_template_options", joinColumns = [JoinColumn(name = "bet_template_id")])
    @Column(name = "option")
    var options: List<String> = ArrayList()
) {

    companion object {
        fun validate(betTemplate: BetTemplate) {
            if(betTemplate.options.size < 2) {
                throw BadRequestException("Multiple choice bets cannot have less than 2 options")
            }
        }
    }

    fun update(betTemplate: BetTemplate) {
        options = betTemplate.options
        name = betTemplate.name
        validate(this)
    }
}
