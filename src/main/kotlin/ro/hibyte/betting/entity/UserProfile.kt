package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.UserProfileDTO


@Entity
data class UserProfile(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long,
    var keycloakId: String? = null,
    var profilePicture: Long? = null,
    var description: String? = null,
    var username: String? = null,

    //bets

    var coins: Number = 50,

    @ElementCollection
    var groups: MutableSet<Long>? = null

){
    constructor(dtoUser: UserProfileDTO): this(
        userId = dtoUser.userId,
        keycloakId = dtoUser.keycloakId,
        profilePicture = dtoUser.profilePicture,
        description = dtoUser.description,
        coins = dtoUser.coins,
        groups = dtoUser.groups
    )

    fun update(dtoUser: UserProfileDTO){
        keycloakId = dtoUser.keycloakId
        profilePicture = dtoUser.profilePicture
        description = dtoUser.description
        coins = dtoUser.coins
        groups = dtoUser.groups
        username = dtoUser.username
    }

}

