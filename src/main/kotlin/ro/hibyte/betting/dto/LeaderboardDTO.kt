package ro.hibyte.betting.dto

data class LeaderboardDTO(
    val id: Long,
    val name: String,
    val entries: List<LeaderboardEntry>
)
