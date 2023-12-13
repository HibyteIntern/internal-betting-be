package ro.hibyte.betting.mapper

import org.springframework.stereotype.Component
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.repository.UserProfileRepository
import java.lang.RuntimeException
import java.util.*

@Service
class BetMapper(private val userProfileRepository: UserProfileRepository) {
    fun mapBetToBetDto(bet: Bet): BetDTO{
        return BetDTO(
            betId = bet.betId,
            user = bet.user?.userId,
            amount = bet.amount,
            odds = bet.odds,
            value = bet.value
       )
    }
    fun mapBetDtoToBet(betDTO: BetDTO): Bet{
        val userId: Long = betDTO.user?:0
        val user = userProfileRepository.findById(userId).orElseThrow{RuntimeException("no such user exists")}
        return Bet(
            betId = betDTO.betId,
            user = user,
            amount = betDTO.amount,
            odds = betDTO.odds,
            value = betDTO.value
        )
    }
}