package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.EventDTO
import ro.hibyte.betting.dto.ResolveOutcomeDTO
import ro.hibyte.betting.dto.UserProfileDTO
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

    fun populateBetTypeOutcome(eventId: Long, resolveOutcomeDTO: ResolveOutcomeDTO) {
        val event = eventRepository.findById(eventId).orElseThrow { RuntimeException("no such event found") }

        event.betTypes.forEach { betType ->
            resolveOutcomeDTO[betType.id]?.let { betTypeService.setFinalOutcome(betType, it) }
        }

        event.betTypes.flatMap { it.bets }.forEach { bet ->
            betService.processBet(bet)
        }
    }

//    fun processBets(eventId: Long) {
//        val event = eventRepository.findById(eventId).orElseThrow { RuntimeException("no such event found") }
//
//        event.bets.forEach { bet ->
//            event.betTypes
//                .find { betType -> betType.id == bet.betType?.id }
//                .ifPresent { matchedBetType ->
//                    if (matchedBetType.finalOutcome == bet.value) {
//                        val amount = bet.amount.toDouble()
//                        val odds = bet.odds
//                        val winnings = amount * odds
//                        bet.user?.coins = (bet.user?.coins?.toDouble() ?: 0.0) + winnings
//                    }
//                }
//        }
//    }
}

