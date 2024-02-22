package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.UserGroup

interface UserGroupRepository: JpaRepository<UserGroup, Long> {
    fun findByGroupName(name: String): UserGroup

    fun findAllByOrderByGroupName(): List<UserGroup>
}
