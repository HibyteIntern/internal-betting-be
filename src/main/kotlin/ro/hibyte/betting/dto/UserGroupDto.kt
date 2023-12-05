package ro.hibyte.betting.dto

import jakarta.persistence.ElementCollection
import ro.hibyte.betting.entity.UserGroup

class UserGroupDto (
    val id: Long,
    var profilePicture: String,
    var description: String,
    var users: Set<Long>
){
    constructor(userGroup: UserGroup): this(
        id = userGroup.id,
        profilePicture = userGroup.profilePicture,
        description = userGroup.description,
        users = userGroup.users
    )
}
