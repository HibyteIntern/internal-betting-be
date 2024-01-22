package ro.hibyte.betting.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.exceptions.types.*
import ro.hibyte.betting.mapper.CompetitionMapper
import ro.hibyte.betting.repository.CompetitionRepository
import java.sql.Timestamp

@Service
class CompetitionService {
    @Autowired
    private lateinit var competitionMapper: CompetitionMapper

    @Autowired
    private lateinit var competitionRepository : CompetitionRepository

    fun getAll(): List<CompetitionDto> {
        val competitions = competitionRepository.findAll()

        return competitions.map { competitionMapper.mapCompetitionToCompetitionDto(it) }
    }

    fun searchByName(name: String): List<CompetitionDto> =
        competitionRepository.findAllByNameContainsIgnoreCase(name).map { competitionMapper.mapCompetitionToCompetitionDto(it) }

    fun getOne(id: Long): Competition = competitionRepository.findById(id).orElseThrow{ EntityNotFoundException("Competition", id) }

    fun create(competitionRequest: CompetitionRequest): Competition = competitionRepository.save(competitionMapper.mapCompetitionRequestToCompetition(competitionRequest))

    fun update(id: Long, competitionRequest: CompetitionRequest): Competition {
        val competition = competitionRepository.findById(id).orElseThrow{ EntityNotFoundException("Competition", id) }

        val newCompetition = competitionMapper.mapCompetitionRequestToCompetition(competitionRequest, competition)

        return competitionRepository.save(newCompetition)
    }

    fun delete(id: Long) = competitionRepository.deleteById(id)
}
