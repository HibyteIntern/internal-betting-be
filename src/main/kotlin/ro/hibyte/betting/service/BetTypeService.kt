package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.exceptions.types.BetTemplateNotFoundException
import ro.hibyte.betting.exceptions.types.BetTypeNotFoundException
import ro.hibyte.betting.repository.BetTemplateRepository
import ro.hibyte.betting.repository.BetTypeRepository

@Service
class BetTypeService(private val betTypeRepository: BetTypeRepository,
                     private val betTemplateRepository: BetTemplateRepository) {

    fun createBetType(betType: BetType): BetType {
        betType.betTemplate = betTemplateRepository.findById(betType.betTemplate.id).orElseThrow{BetTemplateNotFoundException(betType.betTemplate.id)}
        return betTypeRepository.save(betType)
    }
    fun getBetType(id: Long): BetType =
        betTypeRepository.findById(id).orElseThrow{BetTypeNotFoundException(id)}

    fun getBetTypes(): List<BetType> =
        betTypeRepository.findAll()

    fun deleteBetType(id: Long) =
        betTypeRepository.deleteById(id)
}