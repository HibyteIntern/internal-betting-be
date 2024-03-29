package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompleteBetTypeDTO
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository
import ro.hibyte.betting.repository.BetTypeRepository

@Service
class BetTypeService(private val betTypeRepository: BetTypeRepository,
                     private val betTemplateRepository: BetTemplateRepository,
                     private val betTemplateService: BetTemplateService,
) {

    private fun checkExistingBetTemplateAndAssignBetTemplate(betType: BetType): BetType {
        val existingTemplate: BetTemplate? = betTemplateService.checkEntityAlreadyExists(betType.betTemplate)
        if(existingTemplate != null) {
            betType.betTemplate = existingTemplate
        } else {
            BetTemplate.validateAndCorrect(betType.betTemplate)
            betType.betTemplate = betTemplateRepository.save(betType.betTemplate)
        }
        return betType
    }

    fun create(completeType: CompleteBetTypeDTO): BetType {
        val betType = BetType(completeType)
        checkExistingBetTemplateAndAssignBetTemplate(betType)
        return betTypeRepository.save(betType)
    }

    fun getById(id: Long): BetType =
        betTypeRepository.findById(id).orElseThrow{EntityNotFoundException("Bet Type", id)}

    fun getAll(): List<BetType> =
        betTypeRepository.findAll()

    fun update(id: Long, completeType: CompleteBetTypeDTO): BetType {
        val betTypeToUpdate: BetType = betTypeRepository.findById(id).orElseThrow{EntityNotFoundException("Bet Type", id)}
        val betType = BetType(completeType)
        checkExistingBetTemplateAndAssignBetTemplate(betType)
        betTypeToUpdate.update(betType)
        return betTypeRepository.save(betTypeToUpdate)
    }

    fun delete(id: Long) {
        betTypeRepository.findById(id).orElseThrow{EntityNotFoundException("Event Type", id)}
        betTypeRepository.deleteById(id)
    }

    fun setFinalOutcome(betType: BetType, finalOutcome: String) {
        betType.finalOutcome = finalOutcome
        betTypeRepository.save(betType)
    }
}
