package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.UserProfile

data class FullUserProfileDTO(
    val userId: Long? = null,
    var keycloakId: String? = null,
    var username: String? = null,
    var profilePicture: Long? = null,
    var description: String? = null,
    var bets: MutableList<BetDTO>? = null,
    var coins: Number = 50,
    var groups: MutableSet<UserGroupDTO>? = mutableSetOf()
)
{
    constructor(userProfile: UserProfile) : this(
        userId = userProfile.userId,
        keycloakId = userProfile.keycloakId,
        profilePicture = userProfile.profilePicture,
        description = userProfile.description,
        coins = userProfile.coins,
        groups = userProfile.groups?.map{UserGroupDTO(it)}?.toMutableSet(),
        username = userProfile.username,
        bets = userProfile.bets?.map { BetDTO(it) }?.toMutableList()
    )
}
