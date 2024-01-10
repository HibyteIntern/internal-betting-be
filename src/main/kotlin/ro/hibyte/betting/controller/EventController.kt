package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.*
import ro.hibyte.betting.service.EventService

@RestController
@RequestMapping("/api/v1/events")
@CrossOrigin(origins = ["http://localhost:4200"])
class EventController(private val eventService: EventService) {

    @PostMapping("/add")
    fun addEvent(@RequestBody eventRequest: EventDTO): ResponseEntity<Unit> {
        eventService.addEvent(eventRequest)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PutMapping("/edit/{eventId}")
    fun editEvent(@PathVariable eventId: Long, @RequestBody updatedEvent: EventDTO): ResponseEntity<Unit> {
        eventService.editEvent(eventId, updatedEvent)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/delete/{eventId}")
    fun deleteEvent(@PathVariable eventId: Long): ResponseEntity<Unit> {
        eventService.deleteEvent(eventId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventDTO>> {
        val events = eventService.getAllEvents()
        return ResponseEntity(events, HttpStatus.OK)
    }

    @PostMapping("/bet/{eventId}")
    fun addBet(@PathVariable eventId: Long, @RequestBody complexBetByUserDto: ComplexBetByUserDto):ResponseEntity<Unit>{
        eventService.addBetForEvent(eventId,complexBetByUserDto.betDTO,complexBetByUserDto.userProfileDTO)
        return ResponseEntity(HttpStatus.OK)
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

    @GetMapping("/get/name/{name}")
    fun getEventsByName(@PathVariable name:String?):ResponseEntity<List<EventDTO>>{
        val events = name?.let { eventService.getEventsByName(it) } ?: eventService.getAllEvents()
        return ResponseEntity(events,HttpStatus.OK)
    }
    @GetMapping("/get/name/")
    fun getAllEvents2(): ResponseEntity<List<EventDTO>> {
        val events = eventService.getAllEvents()
        return ResponseEntity(events, HttpStatus.OK)
    }

}

