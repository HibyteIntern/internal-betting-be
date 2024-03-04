package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.UserProfile

data class UserProfileDTO(
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
        groups = userProfile.groups?.mapNotNull{it.userGroupId}?.toMutableSet(),
        username = userProfile.username,
    )
    
}


