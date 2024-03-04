package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.BetType

data class BetTypeDTO(
    val id: Long? = null,
    val name: String,
    val options: List<String> = ArrayList(),
    val odds: List<Double> = ArrayList(),
    val outcome: String? = null,
) {
    constructor(betType: BetType) : this(
        id = betType.id,
        name = betType.name,
        options = betType.options,
        odds = betType.odds,
        outcome = betType.finalOutcome
    )
}
