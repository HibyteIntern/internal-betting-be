package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.UserProfileDTO
import java.util.UUID


@Entity
data class UserProfile(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long? = null,
    var keycloakId: String? = null,
    var username: String? = null,
    var profilePicture: Long? = null,
    var description: String? = null,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "userId")
    var bets: Set<Bet>? = null,

    var coins: Number = 50,

    ){
    constructor(dtoUser: UserProfileDTO): this(
        userId = dtoUser.userId,
        keycloakId = dtoUser.keycloakId,
        username = dtoUser.username,
        profilePicture = dtoUser.profilePicture,
        description = dtoUser.description,
        coins = dtoUser.coins,
        bets = dtoUser.bets?.map { Bet(it) }?.toSet()
    )

    fun update(dtoUser: UserProfileDTO){
        username = dtoUser.username
        profilePicture = dtoUser.profilePicture
        description = dtoUser.description
    }


}

