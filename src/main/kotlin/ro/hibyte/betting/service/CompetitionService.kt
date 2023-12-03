package ro.hibyte.betting.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.repository.CompetitionRepository

@Service
class CompetitionService {
    @Autowired
    private lateinit var competitionRepository : CompetitionRepository

    fun getAll(): List<Competition> = competitionRepository.findAll()

    fun getOne(id: Long): Competition = competitionRepository.findById(id).orElseThrow()

    fun create(dto: CompetitionDto): Competition = competitionRepository.save(Competition(dto))

    fun update(dto: CompetitionDto): Competition {
        val competition = competitionRepository.findById(dto.id!!).orElseThrow()

        competition.update(dto)

        return competitionRepository.save(competition)
    }

    fun delete(id: Long) = competitionRepository.deleteById(id)
}