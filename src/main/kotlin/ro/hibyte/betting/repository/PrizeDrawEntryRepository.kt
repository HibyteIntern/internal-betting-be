package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.PrizeDrawEntry

interface PrizeDrawEntryRepository: JpaRepository<PrizeDrawEntry, Long> {
    fun getAllByPrizeDraw_Id(id: Long): List<PrizeDrawEntry>
}
