package ro.hibyte.betting.dto

data class CreateLeaderboardRequest(
    val name: String,
    val events: List<Long>,
    val userProfiles: List<Long>
)
