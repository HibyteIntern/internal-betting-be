package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.EventRequest
import ro.hibyte.betting.dto.EventResponse
import ro.hibyte.betting.service.EventService

@RestController
@RequestMapping("/api/events")
class EventController(private val eventService: EventService) {

    @PostMapping("/add")
    fun addEvent(@RequestBody eventRequest: EventRequest): ResponseEntity<Unit> {
        eventService.addEvent(eventRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PutMapping("/edit/{eventId}")
    fun editEvent(@PathVariable eventId: Long, @RequestBody updatedEvent: EventRequest): ResponseEntity<Unit> {
        eventService.editEvent(eventId, updatedEvent)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/delete/{eventId}")
    fun deleteEvent(@PathVariable eventId: Long): ResponseEntity<Unit> {
        eventService.deleteEvent(eventId)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventResponse>> {
        val events = eventService.getAllEvents()
        return ResponseEntity(events, HttpStatus.OK)
    }

    @GetMapping("/get/{eventId}")
    fun getOneEvent(@PathVariable eventId: Long): ResponseEntity<EventResponse> {
        val event = eventService.getOneEvent(eventId)
        return ResponseEntity(event, HttpStatus.OK)
    }
}

