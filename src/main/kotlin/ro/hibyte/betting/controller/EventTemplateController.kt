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
import ro.hibyte.betting.dto.EventTemplateRequest
import ro.hibyte.betting.entity.EventTemplate
import ro.hibyte.betting.service.EventTemplateService

@RestController
@RequestMapping("/api/event-templates")
@CrossOrigin(origins = ["http://localhost:4200"])
class EventTemplateController(private val eventTemplateService: EventTemplateService) {

    @PostMapping
    fun createEventTemplate(@RequestBody eventTemplateRequest: EventTemplateRequest): ResponseEntity<EventTemplate> =
        ResponseEntity(eventTemplateService.createEventTemplate(eventTemplateRequest), HttpStatus.CREATED)

    @GetMapping("/{id}")
    fun getEventTemplate(@PathVariable id: Long): EventTemplate =
        eventTemplateService.getEventTemplate(id)

    @GetMapping
    fun getEventTemplates(): List<EventTemplate> =
        eventTemplateService.getEventTemplates()

    @PutMapping("/{id}")
    fun updateEventTemplate(@RequestBody eventTemplateRequest: EventTemplateRequest, @PathVariable id: Long): EventTemplate =
        eventTemplateService.updateEventTemplate(eventTemplateRequest, id)

    @DeleteMapping("/{id}")
    fun deleteEventTemplate(@PathVariable id: Long) =
        eventTemplateService.deleteEventTemplate(id)
}