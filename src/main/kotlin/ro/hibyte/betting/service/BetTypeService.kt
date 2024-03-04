package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetTypeDTO
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetTypeRepository

@Service
class BetTypeService(private val betTypeRepository: BetTypeRepository) {

    fun create(betTypeDTO: BetTypeDTO): BetType {
        val betType = BetType(betTypeDTO)
        return betTypeRepository.save(betType)
    }

    fun getById(id: Long): BetType =
        betTypeRepository.findById(id).orElseThrow{EntityNotFoundException("Bet Type", id)}

    fun getAll(): List<BetType> =
        betTypeRepository.findAll()

    fun update(id: Long, betTypeDTO: BetTypeDTO): BetType {
        val betTypeToUpdate: BetType = betTypeRepository.findById(id).orElseThrow{EntityNotFoundException("Bet Type", id)}
        val betType = BetType(betTypeDTO)
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
