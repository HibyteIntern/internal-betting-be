package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import ro.hibyte.betting.dto.UserProfileDTO


@Entity
class UserProfile(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null,
    var keycloakId: String? = null,
    var username: String? = null,
    var profilePicture: Long? = null,
    var description: String? = null,


    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user", fetch = FetchType.EAGER)
    var bets: MutableList<Bet>? = null,


    var coins: Number = 50,

    @ManyToMany( fetch = FetchType.LAZY ,mappedBy = "users")
    @JsonBackReference
    var groups: MutableSet<UserGroup>? = mutableSetOf()

){
    constructor(dtoUser: UserProfileDTO): this(
        userId = dtoUser.userId,
        keycloakId = dtoUser.keycloakId,
        username = dtoUser.username,
        profilePicture = dtoUser.profilePicture,
        description = dtoUser.description,
        coins = dtoUser.coins
    )

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


    fun update(dtoUser: UserProfileDTO) {
        username = dtoUser.username
        profilePicture = dtoUser.profilePicture
        description = dtoUser.description
    }

}


