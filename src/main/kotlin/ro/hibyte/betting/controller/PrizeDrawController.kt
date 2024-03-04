package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.PrizeDrawEntryDTO
import ro.hibyte.betting.dto.PrizeDrawDTO
import ro.hibyte.betting.entity.PrizeDrawEntry
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.service.PrizeDrawService

@RestController
@RequestMapping("api/v1/prize-draws")
@CrossOrigin(origins = ["http://localhost:4200"])
class PrizeDrawController(private val prizeDrawService: PrizeDrawService) {

    @PostMapping
    fun create(@RequestBody prizeDrawDTO: PrizeDrawDTO): ResponseEntity<PrizeDrawDTO> =
        ResponseEntity(
            prizeDrawService.create(prizeDrawDTO),
            HttpStatus.CREATED
        )

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): PrizeDrawDTO =
        prizeDrawService.getById(id)

    @GetMapping
    fun getAll(): List<PrizeDrawDTO> =
        prizeDrawService.getAll()

    @GetMapping("/active")
    fun getActive(): List<PrizeDrawDTO> =
        prizeDrawService.getByStatus(Status.OPEN)

    @GetMapping("/past")
    fun getPast(): List<PrizeDrawDTO> =
        prizeDrawService.getByStatus(Status.CLOSED)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody prizeDrawDTO: PrizeDrawDTO) =
        prizeDrawService.update(id, prizeDrawDTO)


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        prizeDrawService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/entry")
    fun addEntry(@RequestBody prizeDrawEntryDTO: PrizeDrawEntryDTO, authentication: Authentication): PrizeDrawEntry =
        prizeDrawService.addEntry(prizeDrawEntryDTO, authentication)


    @PutMapping("/{id}/end")
    fun end(@PathVariable id: Long): PrizeDrawDTO =
        prizeDrawService.endDraw(id)
}
