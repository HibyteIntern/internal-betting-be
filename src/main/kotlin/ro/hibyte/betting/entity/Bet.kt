package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.BetDTO

@Entity
data class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val betId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "userId")
    var user: UserProfile,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "betTypeId")
    var betType: BetType,

    var amount: Number,
    var odds: Double,
    var value: String,
){
    constructor(
        betDto: BetDTO,
        betType: BetType,
        userProfile: UserProfile,
    ): this(
        betId = betDto.betId,
        user = userProfile,
        betType = betType,
        amount = betDto.amount,
        odds = betType.options.indexOf(betDto.value).let { (betType.odds[it]) },
        value = betDto.value,
    )
}

