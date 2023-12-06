
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.service.BetService

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/bets")
class BetController(private val betService: BetService) {

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
    fun create(@RequestBody betDto: BetDTO): BetDTO {
        val bet = betService.create(betDto)
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

