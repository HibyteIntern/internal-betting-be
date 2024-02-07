package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.FullUserGroupDTO
import ro.hibyte.betting.dto.UserGroupDTO
import ro.hibyte.betting.entity.UserGroup
import ro.hibyte.betting.repository.UserGroupRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class UserGroupService(
    private val userGroupRepository: UserGroupRepository,
    private val waspService: WaspService,
    private val userProfileRepository: UserProfileRepository,
    private val userProfileService: UserProfileService
) {

    fun getAll(): List<FullUserGroupDTO> = userGroupRepository.findAll().map { FullUserGroupDTO(it) }

    fun getOne(id: Long): FullUserGroupDTO =
        userGroupRepository.findById(id)
            .map { FullUserGroupDTO(it) }
            .orElseThrow {
            NoSuchElementException("User Group with id $id was not found")
        }

    fun delete(id: Long) {
        userGroupRepository.findById(id).orElseThrow {
            NoSuchElementException("User Group with id $id was not found")
        }
        userGroupRepository.deleteById(id)
    }

    fun update(id:Long, userGroupDto: FullUserGroupDTO): FullUserGroupDTO {
        val existingUserGroup = userGroupRepository.findById(id).orElseThrow {
            NoSuchElementException("User Group not found with id: ${userGroupDto.userGroupId}")
        }
        existingUserGroup.update(userGroupDto)
        return FullUserGroupDTO(userGroupRepository.save(existingUserGroup))
    }

    fun create(userGroupDto: UserGroupDTO): UserGroupDTO {
        try {
            val userProfiles = userGroupDto.users?.mapNotNull { userId ->
                userProfileService.findById(userId)
            }
            val userGroup = UserGroup(
                groupName = userGroupDto.groupName,
                description = userGroupDto.description,
                users = userProfiles?.toMutableSet()
            ).let{
                userGroupRepository.save(it)
            }
            userProfiles?.forEach {
                it.groups?.add(userGroup)
                userProfileRepository.save(it)
            }
            return UserGroupDTO(userGroup)
        } catch (e: Exception) {
            throw RuntimeException("User Group could not be created", e)
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
