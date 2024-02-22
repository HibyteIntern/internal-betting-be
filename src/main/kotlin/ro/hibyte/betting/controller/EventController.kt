package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.*
import ro.hibyte.betting.service.EventService

@RestController
@RequestMapping("/api/v1/events")
@CrossOrigin(origins = ["http://localhost:4200"])
class EventController(private val eventService: EventService) {

    @PostMapping("/add")
    fun addEvent(@RequestBody eventRequest: EventDTO, authentication: Authentication): ResponseEntity<EventDTO> =
        ResponseEntity(
            eventService.addEvent(eventRequest, authentication.name),
            HttpStatus.CREATED
        )

    @PostMapping("/many")
    fun addEvents(@RequestBody events: List<EventDTO>, authentication: Authentication): ResponseEntity<List<EventDTO>> {
        val response = eventService.addEvents(events, authentication.name)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @PutMapping("/edit/{eventId}")
    fun editEvent(@PathVariable eventId: Long, @RequestBody updatedEvent: EventDTO, authentication: Authentication): ResponseEntity<EventDTO> =
        ResponseEntity(
            eventService.editEvent(eventId, updatedEvent, authentication.name),
            HttpStatus.OK
        )

    @DeleteMapping("/delete/{eventId}")
    fun deleteEvent(@PathVariable eventId: Long, authentication: Authentication): ResponseEntity<Unit> {
        eventService.deleteEvent(eventId, authentication.name)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventDTO>> {
        val events = eventService.getAllEvents()
        return ResponseEntity(events, HttpStatus.OK)
    }

    @GetMapping("/get/{eventId}")
    fun getOneEvent(@PathVariable eventId: Long): ResponseEntity<EventDTO> {
        val event = eventService.getOneEvent(eventId)
        return ResponseEntity(event, HttpStatus.OK)
    }

    @GetMapping("/get/tags")
    fun getTags():ResponseEntity<List<String>>{
        val tags = eventService.getAllTags()
        return ResponseEntity(tags,HttpStatus.OK)
    }

    @GetMapping("/get/name")
    fun getEventsByName(@RequestParam name:String?):ResponseEntity<List<EventDTO>>{
        val events = name?.let { eventService.getEventsByName(it) } ?: eventService.getAllEvents()
        return ResponseEntity(events,HttpStatus.OK)
    }

    @PutMapping("/outcome/{eventId}")
    fun populateBetTypesOutcome(@PathVariable eventId: Long,@RequestBody resolveOutcomeDTO: ResolveOutcomeDTO): ResponseEntity<Unit>{
        eventService.populateBetTypeOutcome(eventId,resolveOutcomeDTO)
        return ResponseEntity(HttpStatus.OK)
    }
}

