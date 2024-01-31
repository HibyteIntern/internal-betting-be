package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.LeaderboardEntry

data class LeaderboardDTO(
    val id: Long,
    val name: String,
    val entries: List<LeaderboardEntry>

)
