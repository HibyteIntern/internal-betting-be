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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.EventTemplateRequest
import ro.hibyte.betting.entity.EventTemplate
import ro.hibyte.betting.service.EventTemplateService

@RestController
@RequestMapping("/api/v1/event-templates")
@CrossOrigin(origins = ["http://localhost:4200"])
class EventTemplateController(private val eventTemplateService: EventTemplateService) {

    @PostMapping
    fun create(@RequestBody eventTemplateRequest: EventTemplateRequest): ResponseEntity<EventTemplate> =
        ResponseEntity(eventTemplateService.create(eventTemplateRequest), HttpStatus.CREATED)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): EventTemplate =
        eventTemplateService.getById(id)

    @GetMapping
    fun getAll(@RequestParam name: String?): List<EventTemplate> =
        if(!name.isNullOrEmpty()) {
            eventTemplateService.searchByName(name)
        } else {
            eventTemplateService.getAll()
        }

    @PutMapping("/{id}")
    fun update(@RequestBody eventTemplateRequest: EventTemplateRequest, @PathVariable id: Long): EventTemplate =
        eventTemplateService.update(eventTemplateRequest, id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        eventTemplateService.delete(id)
        return ResponseEntity.noContent().build()
    }

}
