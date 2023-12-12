package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.EventRequest
import ro.hibyte.betting.dto.EventResponse
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.mapper.BetMapper
import ro.hibyte.betting.mapper.EventMapper
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.UserProfileRepository
import java.lang.RuntimeException
import java.sql.Timestamp
import java.util.stream.Collectors

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val betTypeService: BetTypeService,
    private val betMapper: BetMapper,
    private val betService: BetService,
    private val userProfileService: UserProfileService
) {
    fun addEvent(eventRequest: EventRequest) {
        val event: Event = eventMapper.mapEventRequestToEvent(eventRequest, betTypeService)
        eventRepository.save(event)
    }

    @Transactional
    fun editEvent(eventId: Long, updatedEvent: EventRequest) {
        val existingEvent = eventRepository.findById(eventId)
        if (existingEvent.isPresent) {
            val event = existingEvent.get()
            event.name = updatedEvent.name
            event.description = updatedEvent.description
            event.tags = Regex("#\\w+").findAll(updatedEvent.description)
                .map { it.value }
                .toMutableList();
            event.startsAt = Timestamp.from(updatedEvent.startsAt)
            event.endsAt = Timestamp.from(updatedEvent.endsAt)
            event.status = updatedEvent.status
            eventRepository.save(event)
        } else {
            throw RuntimeException("Event Not Found")
        }
    }

    fun addBetForEvent(eventId: Long, betDTO: BetDTO, userProfileDTO: UserProfileDTO) {

        val bets = userProfileDTO.bets?.stream()
            ?.map(betMapper::mapBetDtoToBet)
            ?.collect(Collectors.toList())

        val userProfile = userProfileService.createUserProfileIfNonExistent(userProfileDTO)

        val existingEvent = eventRepository.findById(eventId)
        if (existingEvent.isPresent) {
            val event = existingEvent.get()
            event.bets.add(betService.create(betDTO, userProfile))
            eventRepository.save(event)
        }
    }


    fun deleteEvent(eventId: Long) {
        eventRepository.deleteById(eventId)
    }

    fun getAllEvents(): List<EventResponse> {
        return eventRepository.findAll()
            .stream()
            .map(eventMapper::mapEventToEventResponse)
            .collect(Collectors.toList())
    }

    fun getOneEvent(eventId: Long): EventResponse {
        val event = eventRepository.findById(eventId)
        if (event.isPresent) {
            return eventMapper.mapEventToEventResponse(event.get())
        } else {
            throw RuntimeException("event not found")
        }
    }
}
