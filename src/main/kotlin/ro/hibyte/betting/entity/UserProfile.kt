package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonBackReference
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

    @ManyToMany( fetch = FetchType.LAZY ,mappedBy = "users")
    @JsonBackReference
    var groups: MutableSet<UserGroup>? = mutableSetOf()

){
    constructor(dtoUser: UserProfileDTO): this(
        userId = dtoUser.userId,
        keycloakId = dtoUser.keycloakId,
        profilePicture = dtoUser.profilePicture,
        description = dtoUser.description,
        username = dtoUser.username,
        coins = dtoUser.coins
    )

    fun update(dtoUser: UserProfileDTO){
        dtoUser.keycloakId?.let { keycloakId = it }
        dtoUser.profilePicture?.let { profilePicture = it }
        dtoUser.description?.let { description = it }
        dtoUser.coins?.let { coins = it }
        dtoUser.username?. let { username = it }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserProfile

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

