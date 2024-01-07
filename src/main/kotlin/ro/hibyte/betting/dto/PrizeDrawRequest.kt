package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.DrawType
import java.time.Instant

data class PrizeDrawRequest (
    var title: String,
    var description: String,
    var endsAt: Instant,
    var prizeDescription: String,
    var type: DrawType,
)
