package ro.hibyte.betting.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.exceptions.types.BetTemplateNotFoundException
import ro.hibyte.betting.repository.CompetitionRepository
import ro.hibyte.betting.repository.EventRepository
import java.time.Instant

@Service
class CompetitionService {
    @Autowired
    private lateinit var competitionRepository : CompetitionRepository
    @Autowired
    private lateinit var eventRepository: EventRepository

    fun getEventsFromIds(eventIDs: List<Long>): List<Event> {
        val events: MutableList<Event> = ArrayList()

        eventIDs.forEach{ id -> events.add(eventRepository.findById(id).orElseThrow{BetTemplateNotFoundException(id)}) }

        return events
    }

    fun getAll(): List<CompetitionDto> {
        val competitions = competitionRepository.findAll()

        return competitions.map { CompetitionDto(it) }
    }

   // fun getOne(id: Long): Competition = competitionRepository.findById(id).orElseThrow()

    fun create(dto: CompetitionRequest): CompetitionDto {
        val competition = CompetitionDto()

        competition.name = dto.name
        competition.creator = ""
        // TODO: users
        competition.userGroups = dto.userGroups
        competition.userProfiles = dto.userProfiles
        competition.events = getEventsFromIds(dto.events)
        competition.created = Instant.now()
        competition.lastModified = Instant.now()
        competition.status = dto.status

        competitionRepository.save(Competition(competition))

        return competition
    }


//    fun update(dto: CompetitionDto): Competition {
//        val competition = competitionRepository.findById(dto.id!!).orElseThrow()
//
//        competition.update(dto)
//
//        return competitionRepository.save(competition)
//    }

    //fun delete(id: Long) = competitionRepository.deleteById(id)
}