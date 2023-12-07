package ro.hibyte.betting.dto


import ro.hibyte.betting.entity.UserGroup

class UserGroupDto (
    val userGroupId: Long,
    var profilePicture: Long?,
    var description: String?,
    var users: MutableSet<UserProfileDTO>? = null
){
    constructor(userGroup: UserGroup): this(
        userGroupId = userGroup.userGroupId,
        profilePicture = userGroup.profilePicture,
        description = userGroup.description,
        users = userGroup.users?.map{UserProfileDTO(it)}?.toMutableSet()
    )
}
