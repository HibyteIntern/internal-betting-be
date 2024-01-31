package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.UserProfile

data class LeaderboardDTO(
    val id: Long,
    val name: String,
    val users: Set<UserProfileDTO>
    //val maps: List<MetricMap>
)
