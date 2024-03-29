package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.UserGroup
import ro.hibyte.betting.entity.UserProfile
import java.util.*

interface UserProfileRepository : JpaRepository<UserProfile, Long>{
    fun findByKeycloakId(keycloakId: String): UserProfile?
    fun findByUsername(userGroupName: String): UserProfile
    fun findAllByUserIdIn(userIds: List<Long>): List<UserProfile>
}
