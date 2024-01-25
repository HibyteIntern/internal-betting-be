package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.Competition
import ro.hibyte.betting.entity.Event

interface CompetitionRepository: JpaRepository<Competition, Long> {
    fun findAllByNameContainsIgnoreCase(name: String): List<Competition>

    fun findCompetitionsByEventsContains(event: Event): List<Competition>
}
