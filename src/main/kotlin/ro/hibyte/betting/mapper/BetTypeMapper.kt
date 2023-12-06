package ro.hibyte.betting.mapper

import ro.hibyte.betting.dto.CompleteBetTypeDto
import ro.hibyte.betting.entity.BetType

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