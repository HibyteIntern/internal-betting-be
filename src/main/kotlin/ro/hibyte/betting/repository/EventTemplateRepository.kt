package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.EventTemplate

interface EventTemplateRepository: JpaRepository<EventTemplate, Long> {
    fun findAllByNameContainsIgnoreCase(name: String): List<EventTemplate>
}
