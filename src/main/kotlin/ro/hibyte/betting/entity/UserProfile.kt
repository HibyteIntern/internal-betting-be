package ro.hibyte.betting.entity

import jakarta.persistence.*
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

    var coins: Number = 50,

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

