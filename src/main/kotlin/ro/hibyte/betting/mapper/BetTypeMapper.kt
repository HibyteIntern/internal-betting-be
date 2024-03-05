package ro.hibyte.betting.mapper

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetTypeDTO
import ro.hibyte.betting.entity.BetType

@Service
class BetTypeMapper {

    fun betTypeToBetTypeDTO(betType: BetType): BetTypeDTO =
        BetTypeDTO(
            id = betType.id,
            name = betType.name,
            options = betType.options,
            odds = betType.odds,
            outcome = betType.finalOutcome
        )
}
