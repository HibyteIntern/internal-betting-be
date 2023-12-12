package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.CompleteBetTypeDto
import ro.hibyte.betting.service.BetTypeService

@RestController
@RequestMapping("/api/v1/bet-types")
@CrossOrigin(origins = ["http://localhost:4200"])
class BetTypeController(private val betTypeService: BetTypeService) {

    @PostMapping
    fun create(@RequestBody completeBet: CompleteBetTypeDto): ResponseEntity<CompleteBetTypeDto> =
        ResponseEntity(
            CompleteBetTypeDto(betTypeService.create(completeBet)),
            HttpStatus.CREATED
        )

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): CompleteBetTypeDto =
       CompleteBetTypeDto(betTypeService.getById(id))

    @GetMapping
    fun getAll(): List<CompleteBetTypeDto> =
        betTypeService.getAll().map{ CompleteBetTypeDto(it) }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody completeBet: CompleteBetTypeDto): ResponseEntity<CompleteBetTypeDto> =
        ResponseEntity(
            CompleteBetTypeDto(betTypeService.update(id, completeBet)),
            HttpStatus.OK
        )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) =
        betTypeService.delete(id)
}
