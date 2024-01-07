package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.repository.PrizeDrawEntryRepository

@Service
class PrizeDrawEntryService(private val prizeDrawEntryRepository: PrizeDrawEntryRepository) {
}
