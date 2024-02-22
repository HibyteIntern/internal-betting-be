package ro.hibyte.betting.dto

import org.springframework.security.core.GrantedAuthority
import ro.hibyte.betting.entity.UserProfile

data class UserProfileDTO(
    val userId: Long? = null,
    var keycloakId: String? = null,
    var username: String? = null,
    var profilePicture: Long? = null,
    var description: String? = null,
    var bets: MutableList<BetDTO>? = null,
    var coins: Number = 50,
    var groups: MutableSet<Long>? = mutableSetOf(),
    var roles: List<String> = listOf()
) {
    constructor(userProfile: UserProfile) : this(
        userId = userProfile.userId,
        keycloakId = userProfile.keycloakId,
        profilePicture = userProfile.profilePicture,
        description = userProfile.description,
        coins = userProfile.coins,
        groups = userProfile.groups?.mapNotNull{it.userGroupId}?.toMutableSet(),
        username = userProfile.username,
        bets = userProfile.bets?.map { BetDTO(it) }?.toMutableList()
    )

    constructor(userProfile: UserProfile, roles: Collection<GrantedAuthority>) : this(
        userId = userProfile.userId,
        keycloakId = userProfile.keycloakId,
        profilePicture = userProfile.profilePicture,
        description = userProfile.description,
        coins = userProfile.coins,
        groups = userProfile.groups?.map{it.userGroupId}?.toMutableSet(),
        username = userProfile.username,
        bets = userProfile.bets?.map { BetDTO(it) }?.toMutableList(),
        roles = roles.map { it.authority }
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
