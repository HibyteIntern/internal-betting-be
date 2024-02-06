package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.EventDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.exceptions.types.EntityNotFoundByNameException
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.exceptions.types.ForbiddenException
import ro.hibyte.betting.mapper.EventMapper
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.CompetitionRepository
import java.sql.Timestamp
import java.time.Instant
import kotlin.RuntimeException

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val betService: BetService,
    private val userProfileService: UserProfileService,
    private val competitionService: CompetitionService,
    private val competitionRepository: CompetitionRepository
) {
    fun addEvent(eventRequest: EventDTO, keycloakId: String): EventDTO {
        val creator = userProfileService.getByKeycloakId(keycloakId) ?: throw EntityNotFoundByNameException("User Profile", keycloakId)
        val event = eventMapper.mapEventRequestToEvent(eventRequest, creator)
        return eventMapper.mapEventToEventResponse(eventRepository.save(event))
    }

    @Transactional
    fun editEvent(eventId: Long, updatedEvent: EventDTO, keycloakId: String): EventDTO {
        val existingEvent = eventRepository.findById(eventId).orElseThrow { EntityNotFoundException("Event", eventId) }
        val user: UserProfile = userProfileService.getByKeycloakId(keycloakId) ?: throw EntityNotFoundByNameException("User Profile", keycloakId)
        if (existingEvent.creator?.userId != user.userId) {
            throw ForbiddenException("You are not allowed to edit this event")
        }
        existingEvent.name = updatedEvent.name?:""
        existingEvent.description = updatedEvent.description?:""
        existingEvent.tags = Regex("#\\w+").findAll(updatedEvent.description?:"")
            .map { it.value }
            .toMutableList()
        existingEvent.startsAt = Timestamp.from(updatedEvent.startsAt)
        existingEvent.endsAt = Timestamp.from(updatedEvent.endsAt)
        existingEvent.status = updatedEvent.status?:Status.DRAFT
        return eventMapper.mapEventToEventResponse(eventRepository.save(existingEvent))
    }

    fun checkEventsEndDate() {
        eventRepository.findAllByStatus(Status.OPEN).forEach { event ->
            run {
                if (event.endsAt.toInstant().isBefore(Instant.now())) {
                    val competitions = competitionRepository.findCompetitionsByEventsContains(event)
                    competitions.forEach { competition ->
                        run {
                            competitionService.checkStatus(competition.competitionId)
                        }
                    }

                    event.status = Status.CLOSED
                    eventRepository.save(event)
                }
            }
        }
    }

    fun addBetForEvent(eventId: Long, betDTO: BetDTO, userProfileDTO: UserProfileDTO) {
        val userProfile = userProfileService.createUserProfileIfNonExistent(userProfileDTO)

        val event = eventRepository.findById(eventId).orElseThrow { RuntimeException("no such event found") }

        event.bets.add(betService.create(betDTO, userProfile))
        eventRepository.save(event)
    }

    fun deleteEvent(eventId: Long, keycloakId: String) {
        val event = eventRepository.findById(eventId).orElseThrow { RuntimeException("No such event found") }
        val user = userProfileService.getByKeycloakId(keycloakId) ?: throw EntityNotFoundByNameException("User Profile", keycloakId)
        if (event.creator?.userId != user.userId) {
            throw ForbiddenException("You are not allowed to delete this event")
        }

        val competitionsWithEvent = competitionRepository.findCompetitionsByEventsContains(event)
        competitionsWithEvent.forEach { competition ->
            run {
                competition.events = competition.events.filterNot { it == event }
                competitionRepository.save(competition)
            }
        }
        eventRepository.deleteById(eventId)
    }

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

