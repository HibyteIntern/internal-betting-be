package ro.hibyte.betting.mapper

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.entity.Bet

@Service
class BetMapper {
    fun mapBetToBetDto(bet: Bet): BetDTO{
        return BetDTO(
            betId = bet.betId,
            user = bet.user?.userId,
            amount = bet.amount,
            odds = bet.odds,
            value = bet.value
       )
    }
}
