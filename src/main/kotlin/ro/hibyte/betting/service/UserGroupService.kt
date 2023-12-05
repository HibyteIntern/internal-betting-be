package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.UserGroupDto
import ro.hibyte.betting.entity.UserGroup
import ro.hibyte.betting.repository.UserGroupRepository

@Service
class UserGroupService(private val userGroupRepository: UserGroupRepository) {
    fun getAll(): List<UserGroup> = userGroupRepository.findAll()
    fun getOne(id: Long): UserGroup =
        userGroupRepository.findById(id).orElseThrow {
            NoSuchElementException("User Group with id $id was not found")
        }
    fun delete(id: Long) {
        userGroupRepository.findById(id).orElseThrow {
            NoSuchElementException("User Group with id $id was not found")
        }
        userGroupRepository.deleteById(id)
    }

    fun update(id: Long, userGroupDto: UserGroupDto): UserGroup {
        val existingUserGroup = userGroupRepository.findById(id).orElseThrow {
            NoSuchElementException("User Group not found with id: ${userGroupDto.id}")
        }

        existingUserGroup.update(userGroupDto)
        return userGroupRepository.save(existingUserGroup)
    }
    fun create(userGroupDto: UserGroupDto) =
        userGroupRepository.save(UserGroup(userGroupDto))
}
