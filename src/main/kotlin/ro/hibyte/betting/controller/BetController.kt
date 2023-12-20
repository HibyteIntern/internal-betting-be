package ro.hibyte.betting.controller

import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.service.BetService
import ro.hibyte.betting.service.UserProfileService

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/bets")
class BetController(private val betService: BetService, private val userProfileService: UserProfileService) {

    @GetMapping
    fun getAll(): Set<BetDTO> {
        val bets = betService.getAll()
        return bets.map { BetDTO(it) }.toSet()
    }

    @GetMapping("/{betId}")
    fun getOne(@PathVariable betId: Long): BetDTO {
        val bet = betService.get(betId)
        return BetDTO(bet)
    }

    @PostMapping("/{userId}")
    fun create(@PathVariable userId: Long, @RequestBody betDto: BetDTO): BetDTO {
        var userProfile = userProfileService.get(userId!!)
        val bet = betService.create(betDto, userProfile)

        return BetDTO(bet)
    }

    @PutMapping("/{betId}")
    fun update(@PathVariable betId: Long, @RequestBody betDto: BetDTO): BetDTO {
        val bet = betService.update(betDto)
        return BetDTO(bet)
    }

    @DeleteMapping("/{betId}")
    fun delete(@PathVariable betId: Long) = betService.delete(betId)
}