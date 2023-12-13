package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile

class BetDTO(
    val betId: Long? = null,
    var user: Long? = null,
    var betType: Long? = null,
    val amount: Number,
    val odds: Double,
    val value: String,
) {
    constructor(bet: Bet): this(
        betId = bet.betId,
        user = bet.user?.userId,
        betType = bet.betType?.id,
        amount = bet.amount,
        odds = bet.odds,
        value = bet.value,
    )
}
