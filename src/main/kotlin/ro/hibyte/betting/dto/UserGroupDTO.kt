package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.UserGroup

data class UserGroupDTO (
    val userGroupId: Long?,
    val groupName: String?,
    var profilePicture: Long?,
    var description: String?,
    var users: MutableSet<Long>? = null
){
    constructor(userGroup: UserGroup): this(
        userGroupId = userGroup.userGroupId,
        groupName = userGroup.groupName,
        profilePicture = userGroup.profilePicture,
        description = userGroup.description,
        users = userGroup.users?.mapNotNull{it.userId}?.toMutableSet()
    )
}
