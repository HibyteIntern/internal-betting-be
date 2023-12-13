package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.entity.BetTemplateType
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository

@Service
class BetTemplateService(private val betTemplateRepository: BetTemplateRepository) {

    fun create(betTemplate: BetTemplate): BetTemplate {
        BetTemplate.validateAndCorrect(betTemplate)
        val existingTemplate = checkEntityAlreadyExists(betTemplate)
        if(existingTemplate != null)
            return existingTemplate
        return betTemplateRepository.save(betTemplate)
    }

    fun getAll(): List<BetTemplate> =
        betTemplateRepository.findAll()

    fun getById(id: Long): BetTemplate =
        betTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Bet Template", id) }

    fun update(id: Long, betTemplate: BetTemplate): BetTemplate {
        val templateToUpdate = betTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Bet Template", id) }
        val existingTemplate = checkEntityAlreadyExists(betTemplate)
        if(existingTemplate != null)
            return existingTemplate
        templateToUpdate.update(betTemplate)
        return betTemplateRepository.save(templateToUpdate)
    }

    fun delete(id: Long) {
        betTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Bet Template", id) }
        betTemplateRepository.deleteById(id)
    }

    /*
    Everytime we want to add a new bet template, we check that a similar template doesn't already exist. The reason we do this is
    to prevent multiple BetTemplates with the same structure filling the database. This way, we make sure that only one template with a
    certain structure can exist at a time.
    */
    fun checkEntityAlreadyExists(betTemplate: BetTemplate): BetTemplate? {
        val betTemplateList: List<BetTemplate> = betTemplateRepository.findBetTemplatesByNameAndType(betTemplate.name, betTemplate.type)
        if(betTemplateList.isEmpty()) return null
        if(betTemplate.type != BetTemplateType.MULTIPLE_CHOICE) return betTemplateList[0]
        for(template in betTemplateList) {
            if(haveSameOptions(betTemplate, template)) return template
        }
        return null
    }

    fun haveSameOptions(template1: BetTemplate, template2: BetTemplate): Boolean =
        template1.multipleChoiceOptions.size == template2.multipleChoiceOptions.size &&
                template1.multipleChoiceOptions.zip(template2.multipleChoiceOptions).all { (option1, option2) -> option1 == option2 }
}
