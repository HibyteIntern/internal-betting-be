package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.FullUserGroupDto
import ro.hibyte.betting.entity.UserGroup
import ro.hibyte.betting.repository.UserGroupRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class UserGroupService(
    private val userGroupRepository: UserGroupRepository,
    private val waspService: WaspService,
    private val userProfileRepository: UserProfileRepository
) {

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

    fun update(id:Long, userGroupDto: FullUserGroupDto): UserGroup {
        val existingUserGroup = userGroupRepository.findById(id).orElseThrow {
            NoSuchElementException("User Group not found with id: ${userGroupDto.userGroupId}")
        }
        existingUserGroup.update(userGroupDto)
        return userGroupRepository.save(existingUserGroup)
    }

    fun create(userGroupDto: FullUserGroupDto): UserGroup {
        try {
            val userGroup = UserGroup(userGroupDto)
            userGroupRepository.save(userGroup)
            userGroup.users?.forEach { userProfile ->
                userProfile.groups?.add(userGroup)
                userProfileRepository.save(userProfile)
            }
            return userGroupRepository.save(userGroup)
        } catch (e: Exception) {
            throw RuntimeException("User Group could not be created")
        }
    }


    fun addPhoto(userId: Long, photo: MultipartFile): Long?{
        val userGroup = userGroupRepository.findById(userId).orElseThrow()
        userGroup.profilePicture = waspService.sendPhotoToWasp(photo)
        userGroupRepository.save(userGroup)
        return userGroup.profilePicture
    }

    fun getPhoto(groupId: Long): ByteArray? {
        val userGroup = userGroupRepository.findById(groupId).orElseThrow()
        val photoId = userGroup.profilePicture ?: throw IllegalArgumentException("Profile picture not set for user group $groupId")
        return waspService.getPhotoFromWasp(photoId)
    }
}
