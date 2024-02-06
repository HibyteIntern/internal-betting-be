package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Bet

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
    constructor(complexBetByUserDto: ComplexBetByUserDto): this(
        user = complexBetByUserDto.user?.userId,
        betType =complexBetByUserDto.betType.id,
        amount = complexBetByUserDto.amount,
        odds = complexBetByUserDto.odds,
        value = complexBetByUserDto.value
    )
}
