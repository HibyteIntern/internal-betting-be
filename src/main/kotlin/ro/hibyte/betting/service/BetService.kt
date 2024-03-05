package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.exceptions.types.BadRequestException
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetRepository
import ro.hibyte.betting.repository.BetTypeRepository
import ro.hibyte.betting.repository.UserProfileRepository
import kotlin.math.ceil
import kotlin.math.roundToInt

@Service
class BetService(
    private val betRepository: BetRepository,
    private val betTypeRepository: BetTypeRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userProfileService: UserProfileService,
) {

    fun getAll(): List<Bet> = betRepository.findAll()

    fun get(betId: Long): Bet {
        return betRepository.findById(betId).orElseThrow {
            NoSuchElementException("Bet not found with betId: $betId")
        }
    }

    fun create(betDto: BetDTO, userProfile: UserProfile): Bet {
        if(userProfile.coins.toInt() < betDto.amount.toInt()) throw BadRequestException("User doesn't have enough coins for request.")

        val betType = betDto.betTypeId.let{
            betTypeRepository.findById(it).orElseThrow { EntityNotFoundException("Bet Type", betDto.betId ?: 0) }}

        val savedBet = betRepository.save(
            Bet(betDto, betType, userProfile)
        )

        betType.bets.add(savedBet)
        betTypeRepository.save(betType)

        userProfile.coins = userProfile.coins.toInt() - savedBet.amount.toInt()
        userProfileRepository.save(userProfile)
        return savedBet
    }

    fun addBetForEvent(betDTO: BetDTO, userProfileDTO: UserProfileDTO) {
        userProfileService.createUserProfileIfNonExistent(userProfileDTO)
    }


    @Transactional
    fun delete(betId: Long) {
        val bet =  betRepository.findById(betId).orElseThrow {
            NoSuchElementException("Bet not found with betId: $betId")
        }

        betRepository.delete(bet)
    }

    fun processBet(bet: Bet) {
        if (bet.value == bet.betType.finalOutcome) {
            bet.user.coins = bet.user.coins.toInt() + (ceil(bet.amount.toDouble() * bet.odds))
            userProfileRepository.save(bet.user)
        }
    }
}

