package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.UserProfile

data class LeaderboardDTO(
    val id: Long,
    val name: String,
    val leaderboardResults: List<List<UserResult>>
    // somehow the users with all their metrics scores and sorted by the desired metric
)

data class UserResult(
    val user: UserProfileDTO,
    val score: Int
)
