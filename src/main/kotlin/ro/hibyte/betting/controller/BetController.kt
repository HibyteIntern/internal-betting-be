package ro.hibyte.betting.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.exceptions.types.EntityNotFoundByNameException
import ro.hibyte.betting.service.BetService
import ro.hibyte.betting.service.UserProfileService

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/v1/bets")
class BetController(
    private val betService: BetService,
    private val userProfileService: UserProfileService
) {

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

    @PostMapping
    fun create(@RequestBody betDto: BetDTO, authentication: Authentication): BetDTO {
        val userProfile = userProfileService.getByKeycloakId(authentication.name)
        if(userProfile != null) {
            val bet = betService.create(betDto, userProfile)
            return BetDTO(bet)
        }
        throw EntityNotFoundByNameException("User Profile", authentication.name)
    }

    @PostMapping("/many")
    fun createMany(@RequestBody betsDto: List<BetDTO>): List<BetDTO> {
        val bets = betsDto.mapNotNull { betDTO ->
            betDTO.userId?.let { userProfileService.get(it) }?.let { betService.create(betDTO, it) }
        }
        return bets.map { BetDTO(it) }
    }

    @DeleteMapping("/{betId}")
    fun delete(@PathVariable betId: Long) = betService.delete(betId)
}
