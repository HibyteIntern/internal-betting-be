package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompleteBetTypeDto
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.exceptions.types.BetTypeNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository
import ro.hibyte.betting.repository.BetTypeRepository

@Service
class BetTypeService(private val betTypeRepository: BetTypeRepository,
                     private val betTemplateRepository: BetTemplateRepository) {

    private fun checkExistingBetTemplateAndAssignBetTemplate(betType: BetType): BetType {
        val existingTemplate: BetTemplate? = BetTemplate.checkEntityAlreadyExists(betType.betTemplate, betTemplateRepository)
        if(existingTemplate != null) {
            betType.betTemplate = existingTemplate
        } else {
            BetTemplate.validateAndCorrect(betType.betTemplate)
            betType.betTemplate = betTemplateRepository.save(betType.betTemplate)
        }
        return betType
    }

    fun createBetType(completeType: CompleteBetTypeDto): BetType {
        val betType = BetType(completeType)
        checkExistingBetTemplateAndAssignBetTemplate(betType)
        return betTypeRepository.save(betType)
    }

    fun getBetType(id: Long): BetType =
        betTypeRepository.findById(id).orElseThrow{BetTypeNotFoundException(id)}

    fun getBetTypes(): List<BetType> =
        betTypeRepository.findAll()

    fun updateBetType(id: Long, completeType: CompleteBetTypeDto): BetType {
        val betTypeToUpdate: BetType = betTypeRepository.findById(id).orElseThrow{BetTypeNotFoundException(id)}
        val betType = BetType(completeType)
        checkExistingBetTemplateAndAssignBetTemplate(betType)
        betTypeToUpdate.update(betType)
        return betTypeRepository.save(betTypeToUpdate)
    }

    fun deleteBetType(id: Long) =
        betTypeRepository.deleteById(id)
}