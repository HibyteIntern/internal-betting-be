package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class UserProfileService(private val userProfileRepository: UserProfileRepository, private val waspService: WaspService) {

    fun getAll(): List<UserProfile> = userProfileRepository.findAll()

    fun get(userId: Long): UserProfile {

        return userProfileRepository.findById(userId).orElseThrow {
            NoSuchElementException("UserProfile not found with userId: $userId")
        }
    }

    fun create(dtoUser: UserProfileDTO): UserProfile {
        val userProfile = UserProfile(dtoUser)
        return userProfileRepository.save(userProfile)
    }

    fun update(dtoUser: UserProfileDTO): UserProfile {
        val userProfile = userProfileRepository.findById(dtoUser.userId!!).orElseThrow {
            NoSuchElementException("UserProfile not found with userId: ${dtoUser.userId}")
        }

        userProfile.update(dtoUser)
        return userProfileRepository.save(userProfile)
    }

    fun delete(userId: Long) {
        if (userProfileRepository.existsById(userId)) {
            userProfileRepository.deleteById(userId)
        } else {
            throw NoSuchElementException("UserProfile not found with userId: $userId")
        }
    }

    fun addPhoto(userId: Long, photo: MultipartFile): Long?{
       var userProfile = userProfileRepository.findById(userId).orElseThrow()
        userProfile.profilePicture = waspService.sendPhotoToWasp(photo)
        userProfileRepository.save(userProfile)
        return userProfile.profilePicture
    }
}