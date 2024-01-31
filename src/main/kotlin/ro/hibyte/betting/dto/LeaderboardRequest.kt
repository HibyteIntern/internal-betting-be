package ro.hibyte.betting.dto

data class LeaderboardRequest(
    val leaderboardId: Long,
    val metrics: List<String> = emptyList(),
    val sortedBy: String = ""
)
