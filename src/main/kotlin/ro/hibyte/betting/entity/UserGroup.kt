package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import ro.hibyte.betting.dto.FullUserGroupDTO

@Entity
class UserGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userGroupId: Long? = 0,
    var groupName: String?,
    var profilePicture: Long? = 0,
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
    fun update(userGroupDto: FullUserGroupDTO){
        userGroupDto.groupName?.let { groupName = it }
        userGroupDto.profilePicture?.let { profilePicture = it }
        userGroupDto.description?.let { description = it }

        userGroupDto.users?.let { updatedUsers ->
            val updatedUserProfiles = updatedUsers.map { UserProfile(it) }.toMutableSet()
            users?.clear()
            users?.addAll(updatedUserProfiles)
        }
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
