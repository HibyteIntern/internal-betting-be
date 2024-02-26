package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.BetTemplateType
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.repository.BetRepository
import ro.hibyte.betting.repository.BetTypeRepository
import ro.hibyte.betting.repository.UserProfileRepository
import kotlin.math.ceil

@Service
class BetService(
    private val betRepository: BetRepository,
    private val betTypeRepository: BetTypeRepository,
    private val userProfileRepository: UserProfileRepository,
) {

    fun getAll(): List<Bet> = betRepository.findAll()

    fun get(betId: Long): Bet {
        return betRepository.findById(betId).orElseThrow {
            NoSuchElementException("Bet not found with betId: $betId")
        }
    }
    @Transactional
    fun getBetsByUserId(userId: Long): List<Bet> {
        val user = userProfileRepository.findById(userId).orElseThrow {
            NoSuchElementException("User not found with userId: $userId")
        }
        return user.bets?.toList() ?: emptyList()
    }

    fun create(betDto: BetDTO, userProfile: UserProfile): Bet {
        val betType = betDto.betType?.let { betTypeRepository.findById(it).orElse(null) }
        if (betType?.betTemplate?.type == BetTemplateType.MULTIPLE_CHOICE) {
            val bet = Bet(betDto, betType)
            bet.user = userProfile

            val savedBet = betRepository.save(bet)

            betType.bets.add(bet)
            betTypeRepository.save(betType)

            userProfile.coins = userProfile.coins.toInt() - bet.amount.toInt()
            userProfileRepository.save(userProfile)

            return savedBet
        }

        throw IllegalArgumentException("BetType with id ${betDto.betType} is not a multiple choice bet type")
    }

    @Transactional
    fun delete(betId: Long) {
        val bet =  betRepository.findById(betId).orElseThrow {
            NoSuchElementException("Bet not found with betId: $betId")
        }

        betRepository.delete(bet)
    }

    fun processBet(bet: Bet) {
        if (bet.value == bet.betType?.finalOutcome) {
            bet.user?.coins = (bet.user?.coins?.toInt() ?: 0) + (ceil(bet.amount.toDouble() * bet.odds))
            userProfileRepository.save(bet.user!!)
        }
    }
}

