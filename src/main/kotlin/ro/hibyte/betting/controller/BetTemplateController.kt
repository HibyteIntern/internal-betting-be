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
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.service.BetTemplateService

@RestController
@RequestMapping("/api/v1/bet-templates")
@CrossOrigin(origins = ["http://localhost:4200"])
class BetTemplateController(private val betTemplateService: BetTemplateService) {

    @PostMapping
    fun create(@RequestBody betTemplate: BetTemplate): ResponseEntity<BetTemplate> =
        ResponseEntity(betTemplateService.create(betTemplate), HttpStatus.CREATED)

    @GetMapping
    fun getAll(): List<BetTemplate> =
        betTemplateService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): BetTemplate =
        betTemplateService.getById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody betTemplate: BetTemplate): ResponseEntity<BetTemplate> =
        ResponseEntity(betTemplateService.update(id, betTemplate), HttpStatus.OK)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) =
        betTemplateService.delete(id)
}
