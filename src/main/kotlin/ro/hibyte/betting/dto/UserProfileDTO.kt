package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.UserProfile

class UserProfileDTO(
    var userId: Long,
    var keycloakId: String? = null,
    var profilePicture: Long? = null,
    var description: String? = null,
    var username: String? = null,
    //bets
    var coins: Number = 50,
    var groups: MutableSet<Long>? = null
) {
    constructor(userProfile: UserProfile) : this(
        userId = userProfile.userId,
        keycloakId = userProfile.keycloakId,
        profilePicture = userProfile.profilePicture,
        description = userProfile.description,
        coins = userProfile.coins,
        groups = userProfile.groups,
        username = userProfile.username
    )
}
