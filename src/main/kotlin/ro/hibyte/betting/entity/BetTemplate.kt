package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ro.hibyte.betting.exceptions.types.BadRequestException

@Entity
data class BetTemplate (
    @Id
    @GeneratedValue
    var id: Long? = null,

    var name: String,

    @Enumerated(EnumType.STRING)
    var type: BetTemplateType,

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "betTemplate", cascade = [CascadeType.ALL])
    var betTypes: List<BetType> = ArrayList(),

    @ElementCollection
    @CollectionTable(name = "bet_template_options", joinColumns = [JoinColumn(name = "bet_template_id")])
    @Column(name = "option")
    var multipleChoiceOptions: List<String> = ArrayList()

) {

    companion object {
        fun validateAndCorrect(betTemplate: BetTemplate) {
            if(betTemplate.type == BetTemplateType.MULTIPLE_CHOICE && betTemplate.multipleChoiceOptions.size < 2) {
                throw BadRequestException("Multiple choice bets cannot have less than 2 options")
            }
            if(betTemplate.type != BetTemplateType.MULTIPLE_CHOICE) {
                betTemplate.multipleChoiceOptions = ArrayList()
            }
        }
    }

    fun update(betTemplate: BetTemplate) {
        multipleChoiceOptions = betTemplate.multipleChoiceOptions
        name = betTemplate.name
        type = betTemplate.type
        validateAndCorrect(this)
    }
}
