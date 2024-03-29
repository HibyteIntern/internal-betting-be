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
import ro.hibyte.betting.dto.CompleteBetTypeDTO
import ro.hibyte.betting.service.BetTypeService

@RestController
@RequestMapping("/api/v1/bet-types")
@CrossOrigin(origins = ["http://localhost:4200"])
class BetTypeController(private val betTypeService: BetTypeService) {

    @PostMapping
    fun create(@RequestBody completeBet: CompleteBetTypeDTO): ResponseEntity<CompleteBetTypeDTO> =
        ResponseEntity(
            CompleteBetTypeDTO(betTypeService.create(completeBet)),
            HttpStatus.CREATED
        )

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): CompleteBetTypeDTO =
       CompleteBetTypeDTO(betTypeService.getById(id))

    @GetMapping
    fun getAll(): List<CompleteBetTypeDTO> =
        betTypeService.getAll().map{ CompleteBetTypeDTO(it) }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody completeBet: CompleteBetTypeDTO): ResponseEntity<CompleteBetTypeDTO> =
        ResponseEntity(
            CompleteBetTypeDTO(betTypeService.update(id, completeBet)),
            HttpStatus.OK
        )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        betTypeService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
