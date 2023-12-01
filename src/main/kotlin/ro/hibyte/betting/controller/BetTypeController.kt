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
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.service.BetTypeService

@RestController
@RequestMapping("/api/bet-types")
@CrossOrigin(origins = ["http://localhost:4200"])
class BetTypeController(private val betTypeService: BetTypeService) {

    @PostMapping
    fun createBetType(@RequestBody betType: BetType): ResponseEntity<BetType> =
        ResponseEntity(betTypeService.createBetType(betType), HttpStatus.CREATED)

    @GetMapping("/{id}")
    fun getBetType(@PathVariable id: Long): BetType =
        betTypeService.getBetType(id)

    @GetMapping
    fun getBetTypes(): List<BetType> =
        betTypeService.getBetTypes()

    @DeleteMapping("/{id}")
    fun deleteBetType(@PathVariable id: Long) =
        betTypeService.deleteBetType(id)
}