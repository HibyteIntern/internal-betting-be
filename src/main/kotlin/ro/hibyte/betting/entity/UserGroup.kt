package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.UserGroupDto

@Entity
data class UserGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userGroupId: Long,
    var profilePicture: Long?,
    var description: String?,

    @ManyToMany
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

//    fun addUser(userProfile: UserProfile) {
//        users?.add(userProfile)
//        userProfile.groups?.add(this.userGroupId)
//    }

}
