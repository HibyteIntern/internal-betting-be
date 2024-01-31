package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.PrizeDraw
import ro.hibyte.betting.entity.Status

interface PrizeDrawRepository: JpaRepository<PrizeDraw, Long> {
    fun findAllByStatus(status: Status): List<PrizeDraw>
}
