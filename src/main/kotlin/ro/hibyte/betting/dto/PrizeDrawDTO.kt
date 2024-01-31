package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.DrawType
import ro.hibyte.betting.entity.PrizeDrawEntry
import ro.hibyte.betting.entity.Status
import ro.hibyte.betting.entity.UserProfile
import java.time.Instant

data class PrizeDrawDTO(
    var id: Long?,
    var title: String,
    var description: String,
    var status: Status?,
    var createdAt: Instant?,
    var endsAt: Instant,
    var prizeDescription: String,
    var type: DrawType,
    var winner: UserProfile?,
    var entries: List<PrizeDrawEntry>?,
    var currentLeader: PrizeDrawEntry?
)
