package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile

class UserProfileDTO(
    val userId: Long? = null,
    var keycloakId: String? = null,
    var username: String? = null,
    var profilePicture: Long? = null,
    var description: String? = null,
        var coins: Number = 50,
    var groups: MutableSet<Long>? = mutableSetOf()
) {
    constructor(userProfile: UserProfile) : this(
        userId = userProfile.userId,
        keycloakId = userProfile.keycloakId,
        profilePicture = userProfile.profilePicture,
        description = userProfile.description,
        coins = userProfile.coins,
        groups = userProfile.groups?.map{it.userGroupId}?.toMutableSet(),
        username = userProfile.username,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserProfileDTO

        if (userId != other.userId) return false
        if (keycloakId != other.keycloakId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + (keycloakId?.hashCode() ?: 0)
        return result
    }

}


