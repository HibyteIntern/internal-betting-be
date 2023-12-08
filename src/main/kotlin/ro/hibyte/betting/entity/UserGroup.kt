package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import ro.hibyte.betting.dto.UserGroupDto

@Entity
data class UserGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userGroupId: Long,
    var profilePicture: Long?,
    var description: String?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(
        name = "user_group_users",
        joinColumns = [JoinColumn(name = "user_group_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableSet<UserProfile>?
){
    constructor(dto: UserGroupDto): this(
        userGroupId = dto.userGroupId,
        profilePicture = dto.profilePicture,
        description = dto.description,
        users = dto.users?.map { UserProfile(it) }?.toMutableSet()
    )
    fun update(userGroupDto: UserGroupDto){
        profilePicture = userGroupDto.profilePicture
        description = userGroupDto.description
        users = userGroupDto.users?.map{UserProfile(it)}?.toMutableSet()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserGroup

        return userGroupId == other.userGroupId
    }

    override fun hashCode(): Int {
        return userGroupId.hashCode()
    }


}
