package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.service.BetTypeService
import ro.hibyte.betting.service.EventService

@Entity
data class Bet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val betId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    var user: UserProfile? = null,

    @OneToOne(fetch = FetchType.LAZY)
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
        odds = betDto.odds,
        value = betDto.value,
    )

    fun update(dtoBet: BetDTO){
        amount = dtoBet.amount
        odds = dtoBet.odds
        value = dtoBet.value

    }
}
