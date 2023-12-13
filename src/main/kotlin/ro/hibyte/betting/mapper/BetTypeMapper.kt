package ro.hibyte.betting.mapper

import org.springframework.stereotype.Component
import ro.hibyte.betting.dto.CompleteBetTypeDto
import ro.hibyte.betting.entity.BetType

@Service
class BetTypeMapper {

    fun betTypeToCompleteBetTypeDto(betType: BetType): CompleteBetTypeDto =
        CompleteBetTypeDto(
            id = betType.id,
            name = betType.betTemplate.name,
            type = betType.betTemplate.type,
            multipleChoiceOptions = betType.betTemplate.multipleChoiceOptions,
            odds = betType.odds
        )
}
