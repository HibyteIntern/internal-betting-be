package ro.hibyte.betting.dto

import ro.hibyte.betting.entity.Role
import ro.hibyte.betting.entity.UserProfile

data class UserPermissions(
    val userProfile: UserProfile?,
    val role: Role = Role.USER
)
