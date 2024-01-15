package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.PrizeDrawEntryRequest
import ro.hibyte.betting.dto.PrizeDrawRequest
import ro.hibyte.betting.dto.PrizeDrawResponse
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

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody prizeDrawRequest: PrizeDrawRequest) =
        prizeDrawService.update(id, prizeDrawRequest)


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        prizeDrawService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/entry")
    fun addEntry(@RequestBody prizeDrawEntryRequest: PrizeDrawEntryRequest, authentication: Authentication) {
        val jwt = (authentication as JwtAuthenticationToken).principal as Jwt
        prizeDrawService.addEntry(prizeDrawEntryRequest, jwt.subject)
    }
}
