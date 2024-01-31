package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.Competition

interface CompetitionRepository: JpaRepository<Competition, Long> {
    fun findAllByNameContainsIgnoreCase(name: String): List<Competition>
}
