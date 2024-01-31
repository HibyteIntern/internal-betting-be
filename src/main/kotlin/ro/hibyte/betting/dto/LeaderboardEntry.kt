package ro.hibyte.betting.dto

data class LeaderboardEntry(
    val userProfile: UserProfileDTO,
    val metrics: List<MetricDTO>
)
