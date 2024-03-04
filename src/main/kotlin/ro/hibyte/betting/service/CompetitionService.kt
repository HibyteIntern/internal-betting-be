package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.hibernate.Hibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDTO
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.exceptions.types.*
import ro.hibyte.betting.mapper.CompetitionMapper
import ro.hibyte.betting.repository.CompetitionRepository

@Service
class CompetitionService {
    @Autowired
    private lateinit var competitionMapper: CompetitionMapper

    @Autowired
    private lateinit var competitionRepository : CompetitionRepository

    fun getAll(): List<CompetitionDTO> {
        val competitions = competitionRepository.findAll()

        return competitions.map { competitionMapper.mapCompetitionToCompetitionDto(it) }
    }

    fun searchByName(name: String): List<CompetitionDTO> =
        competitionRepository.findAllByNameContainsIgnoreCase(name).map { competitionMapper.mapCompetitionToCompetitionDto(it) }

    fun getOne(id: Long): Competition = competitionRepository.findById(id).orElseThrow{ EntityNotFoundException("Competition", id) }

    fun create(competitionRequest: CompetitionRequest): Competition = competitionRepository.save(competitionMapper.mapCompetitionRequestToCompetition(competitionRequest))

    fun update(id: Long, competitionRequest: CompetitionRequest): Competition {
        val competition = competitionRepository.findById(id).orElseThrow{ EntityNotFoundException("Competition", id) }

        val newCompetition = competitionMapper.mapCompetitionRequestToCompetition(competitionRequest, competition)

        return competitionRepository.save(newCompetition)
    }

    @Transactional
    fun checkStatus(competitionId: Long) {
        val competition = competitionRepository.findById(competitionId).orElseThrow{ EntityNotFoundException("Competition", competitionId) }
        Hibernate.initialize(competition.events)

        var completedEvents = 0

        competition.events.forEach { event ->
            run {
                if(event.status == Status.CLOSED) {
                    completedEvents++
                }
            }
        }

        if (completedEvents == competition.events.size) {
            competition.status = Status.CLOSED
            competitionRepository.save(competition)
        }
    }

    fun delete(id: Long) = competitionRepository.deleteById(id)
}
