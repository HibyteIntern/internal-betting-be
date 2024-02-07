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
    var user: UserProfile? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "betTypetId")
    var betType: BetType? = null,

    var amount: Number,
    var odds: Double,
    var value: String,
){
    constructor(betDto: BetDTO, betType: BetType?): this(
        betId = betDto.betId,
        user = betDto.user?.let { UserProfile(userId = it) },
        betType = betType,
        amount = betDto.amount,
        odds = betType?.betTemplate?.multipleChoiceOptions?.indexOf(betDto.value)?.let { (betType.odds[it]) } ?: 0.0,
        value = betDto.value,
    )
}

