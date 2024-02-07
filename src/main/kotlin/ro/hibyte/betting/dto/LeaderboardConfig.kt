package ro.hibyte.betting.dto

data class LeaderboardConfig(
    val id: Long? = null,
    val name: String,
    val events: List<Long> = emptyList(),
    val userProfiles: List<Long> = emptyList(),
)
