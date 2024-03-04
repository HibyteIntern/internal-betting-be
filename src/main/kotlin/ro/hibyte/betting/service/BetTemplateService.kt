package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository

@Service
class BetTemplateService(private val betTemplateRepository: BetTemplateRepository) {

    fun create(betTemplate: BetTemplate): BetTemplate {
        BetTemplate.validate(betTemplate)
        return betTemplateRepository.save(betTemplate)
    }

    fun getAll(): List<BetTemplate> =
        betTemplateRepository.findAll()

    fun getById(id: Long): BetTemplate =
        betTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Bet Template", id) }

    fun update(id: Long, betTemplate: BetTemplate): BetTemplate {
        val templateToUpdate = betTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Bet Template", id) }
        templateToUpdate.update(betTemplate)
        return betTemplateRepository.save(templateToUpdate)
    }

    fun delete(id: Long) {
        betTemplateRepository.findById(id).orElseThrow { EntityNotFoundException("Bet Template", id) }
        betTemplateRepository.deleteById(id)
    }
}
