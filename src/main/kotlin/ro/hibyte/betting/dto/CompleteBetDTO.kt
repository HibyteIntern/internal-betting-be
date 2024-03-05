package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.exceptions.types.BadRequestException

data class CompleteBetDTO(
    val betId: Long? = null,
    var user: FullUserProfileDTO? = null,
    var betTypeId: Long,
    val amount: Number,
    val odds: Double? = null,
    val eventId: Long? = null,
    val value: String,
) {
    constructor(bet: Bet): this(
        betId = bet.betId,
        user = FullUserProfileDTO(bet.user),
        betTypeId = bet.betType.id ?: throw BadRequestException("Bet Type must have an id."),
        amount = bet.amount,
        odds = bet.odds,
        value = bet.value,
        eventId = bet.betType.event?.eventId
    )
}
