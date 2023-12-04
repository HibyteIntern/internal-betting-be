package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.UserProfile
import java.util.UUID

class UserProfileDTO(
    var userId: Long? = null,
    var keycloakId: String? = null,
    var profilePicture: String? = null,
    var description: String? = null,
    //bets
    var coins: Number? = null,
) {
    constructor(userProfile: UserProfile) : this(
        userId = userProfile.userId,
        keycloakId = userProfile.keycloakId,
        profilePicture = userProfile.profilePicture,
        description = userProfile.description,
        coins = userProfile.coins,
    )
}