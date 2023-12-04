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
@RequestMapping("/api/bet-templates")
@CrossOrigin(origins = ["http://localhost:4200"])
class BetTemplateController(private val betTemplateService: BetTemplateService) {

    @PostMapping
    fun createBetTemplate(@RequestBody betTemplate: BetTemplate): ResponseEntity<BetTemplate> =
        ResponseEntity(betTemplateService.createBetTemplate(betTemplate), HttpStatus.CREATED)

    @GetMapping
    fun getBetTemplates(): List<BetTemplate> =
        betTemplateService.getBetTemplates()

    @GetMapping("/{id}")
    fun getBetTemplateById(@PathVariable id: Long): BetTemplate =
        betTemplateService.getBetTemplateById(id)

    @PutMapping("/{id}")
    fun updateBetTemplate(@PathVariable id: Long, @RequestBody betTemplate: BetTemplate): ResponseEntity<BetTemplate> =
        ResponseEntity(betTemplateService.updateBetTemplate(id, betTemplate), HttpStatus.OK)

    @DeleteMapping("/{id}")
    fun deleteBetTemplate(@PathVariable id: Long) =
        betTemplateService.deleteBetTemplate(id)
}