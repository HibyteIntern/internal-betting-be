package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.exceptions.types.ConflictException
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository

@Service
class BetTemplateService(private val betTemplateRepository: BetTemplateRepository) {

    fun create(betTemplate: BetTemplate): BetTemplate {
        BetTemplate.validate(betTemplate)
        val existingTemplate = checkEntityAlreadyExists(betTemplate)
        if(existingTemplate != null)
            throw ConflictException("Bet template with this name and options already exists.")
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
            throw ConflictException("Bet template with this name and options already exists.")
        templateToUpdate.update(betTemplate)
        return betTemplateRepository.save(templateToUpdate)
    }

    fun delete(id: Long) {
        betTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Bet Template", id) }
        betTemplateRepository.deleteById(id)
    }

    fun checkEntityAlreadyExists(betTemplate: BetTemplate): BetTemplate? {
        val betTemplateList: List<BetTemplate> = betTemplateRepository.findBetTemplatesByName(betTemplate.name)
        if(betTemplateList.isEmpty()) return null
        for(template in betTemplateList) {
            if(haveSameOptions(betTemplate, template)) return template
        }
        return null
    }

    fun haveSameOptions(template1: BetTemplate, template2: BetTemplate): Boolean =
        template1.options.size == template2.options.size &&
                template1.options.zip(template2.options).all { (option1, option2) -> option1 == option2 }
}
