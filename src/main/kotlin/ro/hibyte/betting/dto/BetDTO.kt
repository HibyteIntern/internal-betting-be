package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile

class BetDTO(
    val betId: Long? = null,
    val user: UserProfile,

    //event
    //betType

    val amount: Number,
    val odds: Double,
    val value: String,
) {
    constructor(bet: Bet): this(
        betId = bet.betId,
        user = bet.user,
        amount = bet.amount,
        odds = bet.odds,
        value = bet.value,
    )
}