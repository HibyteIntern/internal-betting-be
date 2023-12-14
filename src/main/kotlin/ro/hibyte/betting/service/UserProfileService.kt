package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class UserProfileService(private val userProfileRepository: UserProfileRepository, private val waspService: WaspService) {


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

    fun getId(userProfile: UserProfile): Long?{
        return userProfile.userId}

    fun createUserProfileIfNonExistent(userProfileDTO: UserProfileDTO): UserProfile{
        val userId: Long = userProfileDTO.userId?:0
        var userProfile = userProfileRepository.findById(userId)
        if (userProfile.isPresent){
            return userProfile.get()
        }
        else{
            val user= UserProfile(userProfileDTO)
            return userProfileRepository.save(user)
        }
    }
}