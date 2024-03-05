package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.exceptions.types.BadRequestException

class BetDTO(
    val betId: Long? = null,
    var userId: Long? = null,
    var betTypeId: Long,
    val amount: Number,
    val odds: Double? = null,
    val eventId: Long? = null,
    val value: String,
) {
    constructor(bet: Bet): this(
        betId = bet.betId,
        userId = bet.user.userId,
        betTypeId = bet.betType.id ?: throw BadRequestException("Bet Type must have an id."),
        amount = bet.amount,
        odds = bet.odds,
        value = bet.value,
        eventId = bet.betType.event?.eventId
    )
}
