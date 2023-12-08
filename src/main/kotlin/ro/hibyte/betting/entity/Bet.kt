package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.BetDTO

@Entity
data class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val betId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    var user: UserProfile? = null,

    //event
    //betType

    var amount: Number,
    var odds: Double,
    var value: String,
){
    constructor(betDto: BetDTO): this(
        betId = betDto.betId,
        amount = betDto.amount,
        odds = betDto.odds,
        value = betDto.value,
    )

    fun update(dtoBet: BetDTO){
        amount = dtoBet.amount
        odds = dtoBet.odds
        value = dtoBet.value

    }
}
