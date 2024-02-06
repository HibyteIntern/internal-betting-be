package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.ComplexBetByUserDto
import ro.hibyte.betting.dto.EventDTO
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.mapper.BetMapper
import ro.hibyte.betting.mapper.EventMapper
import ro.hibyte.betting.repository.EventRepository
import java.sql.Timestamp
import kotlin.RuntimeException

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val betTypeService: BetTypeService,
    private val betMapper: BetMapper,
    private val betService: BetService,
    private val userProfileService: UserProfileService
) {
    fun addEvent(eventRequest: EventDTO) {
        val event: Event = eventMapper.mapEventRequestToEvent(eventRequest, betTypeService)
        eventRepository.save(event)
    }

    @Transactional
    fun editEvent(eventId: Long, updatedEvent: EventDTO) {
        val existingEvent = eventRepository.findById(eventId).orElseThrow { RuntimeException("event not present") }
        existingEvent.name = updatedEvent.name?:""
        existingEvent.description = updatedEvent.description?:""
        existingEvent.tags = Regex("#\\w+").findAll(updatedEvent.description?:"")
            .map { it.value }
            .toMutableList();
        existingEvent.startsAt = Timestamp.from(updatedEvent.startsAt)
        existingEvent.endsAt = Timestamp.from(updatedEvent.endsAt)
        existingEvent.status = updatedEvent.status?:Status.DRAFT
        eventRepository.save(existingEvent)
    }

    fun addBetForEvent(eventId: Long, complexBetByUserDto: ComplexBetByUserDto,) {
        val userProfile = complexBetByUserDto.user?.let { userProfileService.createUserProfileIfNonExistent(it) }

        val event = eventRepository.findById(eventId).orElseThrow { RuntimeException("no such event found") }

        val betDTO = BetDTO(complexBetByUserDto)
        println(betDTO.betType)
        val bet = userProfile?.let { betService.create(betDTO, it) }
        if (bet != null) {
            event.bets.add(bet)
        }
        println(bet?.betType?.id)

        eventRepository.save(event)
    }


    fun deleteEvent(eventId: Long) = eventRepository.deleteById(eventId)
    fun getAllEvents(): List<EventDTO> {
        return eventRepository.findAll()
            .map(eventMapper::mapEventToEventResponse)
            .toList()
    }

    fun getOneEvent(eventId: Long): EventDTO {
        val event = eventRepository.findById(eventId).orElseThrow { RuntimeException("no such event found") }
        return eventMapper.mapEventToEventResponse(event)
    }

    fun getAllTags():List<String>{
        return eventRepository.findAll()
            .flatMap { event -> eventMapper.mapToTags(event) }
            .distinct()
            .toList()
    }

    fun getEventsByName(name:String):List<EventDTO>{
        return eventRepository.findAllByNameContainsIgnoreCase(name)
              .map(eventMapper::mapEventToEventResponse)
              .toList()
    }
}

