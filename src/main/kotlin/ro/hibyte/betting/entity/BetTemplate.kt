package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ro.hibyte.betting.exceptions.types.BadRequestException
import ro.hibyte.betting.repository.BetTemplateRepository


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
        //this method ensures that no MULTIPLE_CHOICE BetTemplates can be created if the number of options is less than 2
        fun validateAndCorrect(betTemplate: BetTemplate) {
            if(betTemplate.type == BetTemplateType.MULTIPLE_CHOICE && betTemplate.multipleChoiceOptions.size < 2) {
                throw BadRequestException("Multiple choice bets cannot have less than 2 options")
            }
            if(betTemplate.type != BetTemplateType.MULTIPLE_CHOICE) {
                betTemplate.multipleChoiceOptions = ArrayList()
            }
        }

        //this method checks if the betTemplate already exists in the database. If it does, it returns the existing entity, otherwise null
        //equality criteria: same name, same type, and if the type is MULTIPLE_CHOICE, same option list
        fun checkEntityAlreadyExists(betTemplate: BetTemplate, betTemplateRepository: BetTemplateRepository): BetTemplate? {
            val betTemplateList: List<BetTemplate> = betTemplateRepository.findBetTemplatesByNameAndType(betTemplate.name, betTemplate.type)
            if(betTemplateList.isEmpty()) return null
            if(betTemplate.type != BetTemplateType.MULTIPLE_CHOICE) return betTemplateList[0]

            //if the type is MULTIPLE_CHOICE, we must also iterate through the list and check if the options are the same
            for(template in betTemplateList) {
                var sameOptions = true
                if (template.multipleChoiceOptions.size != betTemplate.multipleChoiceOptions.size) continue
                for (i in 0 until template.multipleChoiceOptions.size) {
                    if (template.multipleChoiceOptions[i] != betTemplate.multipleChoiceOptions[i]) {
                        sameOptions = false
                        break
                    }
                }
                if(sameOptions) return template
            }
            return null
        }
    }

    fun update(betTemplate: BetTemplate) {
        multipleChoiceOptions = betTemplate.multipleChoiceOptions
        name = betTemplate.name
        type = betTemplate.type
        validateAndCorrect(this)
    }
}
