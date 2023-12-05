package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.UserGroupDto

@Entity
data class UserGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var profilePicture: String,
    var description: String,
    @ElementCollection
    var users: Set<Long>
){
    constructor(dto: UserGroupDto): this(
        id = dto.id,
        profilePicture = dto.profilePicture,
        description = dto.description,
        users = dto.users
    )
    fun update(userGroupDto: UserGroupDto){
        profilePicture = userGroupDto.profilePicture
        description = userGroupDto.description
        users = userGroupDto.users
    }
}
