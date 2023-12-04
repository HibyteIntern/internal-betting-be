package ro.hibyte.betting.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import ro.hibyte.betting.dto.UserProfileDTO
import java.util.UUID


@Entity
data class UserProfile(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,
    var keycloakId: String? = null,
    var profilePicture: String? = null,
    var description: String? = null,
    //bets
    var coins: Number? = null,

){
    constructor(dtoUser: UserProfileDTO): this(
        userId = dtoUser.userId,
        keycloakId = dtoUser.keycloakId,
        profilePicture = dtoUser.profilePicture,
        description = dtoUser.description,
        coins = dtoUser.coins,
    )

    fun update(dtoUser: UserProfileDTO){
        keycloakId = dtoUser.keycloakId
        profilePicture = dtoUser.profilePicture
        description = dtoUser.description
        coins = dtoUser.coins
    }


}

