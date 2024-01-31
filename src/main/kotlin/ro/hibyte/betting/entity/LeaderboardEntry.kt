package ro.hibyte.betting.entity

data class LeaderboardEntry(
    val userId: Long,
    val metrics: Map<String, Int>
)
