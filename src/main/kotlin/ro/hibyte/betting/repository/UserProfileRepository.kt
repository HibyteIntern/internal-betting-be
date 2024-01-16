package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.UserProfile
import java.util.*

interface UserProfileRepository : JpaRepository<UserProfile, Long> {
    fun findByKeycloakId(keycloakId: String): Optional<UserProfile>
}
