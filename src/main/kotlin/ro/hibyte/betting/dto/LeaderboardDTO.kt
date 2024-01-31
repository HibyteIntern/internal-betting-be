package ro.hibyte.betting.dto

import java.time.Instant

data class LeaderboardDTO(
    var startDate: Instant? = null,
    var endDate: Instant? = null,
    var usersInLeaderboard: List<String>? = null,
    var leaderboardSorted: List<UserProfileDTO>? =null
)
