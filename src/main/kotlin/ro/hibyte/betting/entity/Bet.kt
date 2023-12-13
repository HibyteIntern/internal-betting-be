package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.BetDTO

@Entity
data class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val betId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    val user: UserProfile,

    //event
    //betType

    val amount: Number,
    val odds: Double,
    val value: String,
){
    constructor(betDto: BetDTO): this(
        betId = betDto.betId,
        user = betDto.user,
        amount = betDto.amount,
        odds = betDto.odds,
        value = betDto.value,
    )
}
