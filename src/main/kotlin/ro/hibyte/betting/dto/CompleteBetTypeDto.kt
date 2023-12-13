package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.BetTemplateType
import ro.hibyte.betting.entity.BetType

data class CompleteBetTypeDto(
    val id: Long? = null,
    val name: String,
    val type: String,
    val multipleChoiceOptions: List<String>? = null,
    val odds: List<Double>? = null
) {
    constructor(betType: BetType) : this(
        id = betType.id,
        name = betType.betTemplate.name,
        type = betType.betTemplate.type.name,
        multipleChoiceOptions = betType.betTemplate.multipleChoiceOptions,
        odds = betType.odds
    )
}
