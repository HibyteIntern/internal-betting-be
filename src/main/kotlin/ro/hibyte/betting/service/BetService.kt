package ro.hibyte.betting.service

import ro.hibyte.betting.repository.BetRepository

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile

@Service
class BetService(private val betRepository: BetRepository) {

    fun getAll(): List<Bet> = betRepository.findAll()

    fun get(betId: Long): Bet {
        return betRepository.findById(betId).orElseThrow {
            NoSuchElementException("Bet not found with betId: $betId")
        }
    }

    fun create(dto: BetDTO, userProfile: UserProfile): Bet {
        val bet = Bet(dto)
        bet.user = userProfile
        return betRepository.save(bet)
    }

    fun update(dtoBet: BetDTO): Bet {
        val bet = betRepository.findById(dtoBet.betId!!).orElseThrow {
            NoSuchElementException("Bet not found with betId: ${dtoBet.betId}")
        }

        bet.update(dtoBet)

        return betRepository.save(bet)
    }

    fun delete(betId: Long) {
        if (betRepository.existsById(betId)) {
            betRepository.deleteById(betId)
        } else {
            throw NoSuchElementException("Bet not found with betId: $betId")
        }
    }


}
