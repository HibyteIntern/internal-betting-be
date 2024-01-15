package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import ro.hibyte.betting.dto.UserGroupDto

@Entity
data class UserGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userGroupId: Long,
    var groupName: String?,
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
        groupName = dto.groupName,
        profilePicture = dto.profilePicture,
        description = dto.description,
        users = dto.users?.map { UserProfile(it) }?.toMutableSet()
    )
    fun update(userGroupDto: UserGroupDto){
        userGroupDto.groupName?.let { groupName = it }
        userGroupDto.profilePicture?.let { profilePicture = it }
        userGroupDto.description?.let { description = it }

        userGroupDto.users?.let { updatedUsers ->
            val updatedUserProfiles = updatedUsers.map { UserProfile(it) }.toMutableSet()
            users?.clear()
            users?.addAll(updatedUserProfiles)
        }
    }
}
