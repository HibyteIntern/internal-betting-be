package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.PrizeDrawEntryRequest
import ro.hibyte.betting.dto.PrizeDrawRequest
import ro.hibyte.betting.dto.PrizeDrawResponse
import ro.hibyte.betting.entity.PrizeDrawEntry
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.service.PrizeDrawService

@RestController
@RequestMapping("api/v1/prize-draws")
@CrossOrigin(origins = ["http://localhost:4200"])
class PrizeDrawController(private val prizeDrawService: PrizeDrawService) {

    @PostMapping
    fun create(@RequestBody prizeDrawRequest: PrizeDrawRequest): ResponseEntity<PrizeDrawResponse> =
        ResponseEntity(
            prizeDrawService.create(prizeDrawRequest),
            HttpStatus.CREATED
        )

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): PrizeDrawResponse =
        prizeDrawService.getById(id)

    @GetMapping
    fun getAll(): List<PrizeDrawResponse> =
        prizeDrawService.getAll()

    @GetMapping("/active")
    fun getActive(): List<PrizeDrawResponse> =
        prizeDrawService.getByStatus(Status.OPEN)

    @GetMapping("/past")
    fun getPast(): List<PrizeDrawResponse> =
        prizeDrawService.getByStatus(Status.CLOSED)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        prizeDrawService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/entry")
    fun addEntry(@RequestBody prizeDrawEntryRequest: PrizeDrawEntryRequest): PrizeDrawEntry =
        prizeDrawService.addEntry(prizeDrawEntryRequest)
}
