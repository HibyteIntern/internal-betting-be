package ro.hibyte.betting.dto


import ro.hibyte.betting.entity.UserGroup

data class UserGroupDto (
    val userGroupId: Long,
    val groupName: String?,
    var profilePicture: Long?,
    var description: String?,
    var users: MutableSet<UserProfileDTO>? = null
){
    constructor(userGroup: UserGroup): this(
        userGroupId = userGroup.userGroupId,
        groupName = userGroup.groupName,
        profilePicture = userGroup.profilePicture,
        description = userGroup.description,
        users = userGroup.users?.map{UserProfileDTO(it)}?.toMutableSet()
    )
}
