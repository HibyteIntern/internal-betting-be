package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.UserProfile

interface UserProfileRepository : JpaRepository<UserProfile, Long>