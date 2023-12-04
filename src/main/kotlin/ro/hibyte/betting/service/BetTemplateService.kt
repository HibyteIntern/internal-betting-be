package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.exceptions.types.BetTemplateNotFoundException
import ro.hibyte.betting.exceptions.types.EntityAlreadyExistsException
import ro.hibyte.betting.repository.BetTemplateRepository

@Service
class BetTemplateService(private val betTemplateRepository: BetTemplateRepository) {

    fun createBetTemplate(betTemplate: BetTemplate): BetTemplate {
        BetTemplate.validateAndCorrect(betTemplate)
        if(BetTemplate.checkEntityAlreadyExists(betTemplate, betTemplateRepository) != null)
            throw EntityAlreadyExistsException("Bet template with this name, type and options already exists")
        return betTemplateRepository.save(betTemplate)
    }

    fun getBetTemplates(): List<BetTemplate> =
        betTemplateRepository.findAll()

    fun getBetTemplateById(id: Long): BetTemplate =
        betTemplateRepository.findById(id).orElseThrow { BetTemplateNotFoundException(id) }

    fun updateBetTemplate(id: Long, betTemplate: BetTemplate): BetTemplate {
        val templateToUpdate = betTemplateRepository.findById(id).orElseThrow { BetTemplateNotFoundException(id) }
        if(BetTemplate.checkEntityAlreadyExists(betTemplate, betTemplateRepository) != null)
            throw EntityAlreadyExistsException("Bet template with this name, type and options already exists")
        templateToUpdate.update(betTemplate)
        return betTemplateRepository.save(templateToUpdate)
    }

    fun deleteBetTemplate(id: Long) {
        betTemplateRepository.findById(id).orElseThrow { BetTemplateNotFoundException(id) }
        betTemplateRepository.deleteById(id)
    }
}