package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.BetTemplate
import ro.hibyte.betting.entity.BetTemplateType

interface BetTemplateRepository: JpaRepository<BetTemplate, Long> {
    fun findBetTemplatesByNameAndType(name: String, type: BetTemplateType): List<BetTemplate>
}
