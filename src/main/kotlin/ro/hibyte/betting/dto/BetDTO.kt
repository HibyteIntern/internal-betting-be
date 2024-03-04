package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.exceptions.types.BadRequestException

class BetDTO(
    val betId: Long? = null,
    var user: Long? = null,
    var betType: Long,
    val amount: Number,
    val odds: Double? = null,
    val event: Long? = null,
    val value: String,
) {
    constructor(bet: Bet): this(
        betId = bet.betId,
        user = bet.user.userId,
        betType = bet.betType.id ?: throw BadRequestException("Bet Type must have an id."),
        amount = bet.amount,
        odds = bet.odds,
        value = bet.value,
    )
}
