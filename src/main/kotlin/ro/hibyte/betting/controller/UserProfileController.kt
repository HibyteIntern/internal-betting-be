package ro.hibyte.betting.controller


import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.PrizeDrawEntryDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.service.BetService
import ro.hibyte.betting.service.UserProfileService


@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/v1/user-profile")
class UserProfileController(private val userProfileService: UserProfileService) {

    @GetMapping
    fun getAll(): List<UserProfileDTO>{
        val userProfiles = userProfileService.getAll()
        return userProfiles.map { UserProfileDTO(it) }
    }

    @GetMapping("/getMe")
    fun getMe(authentication: Authentication): UserProfileDTO {
        val userProfile = userProfileService.getByKeycloakId(authentication.name)
        if (userProfile != null) {
            return UserProfileDTO(userProfile)
        } else {

            val newUserProfile = UserProfile()
            newUserProfile.keycloakId = authentication.name

            val createdUserProfile = userProfileService.create(UserProfileDTO(newUserProfile))

            return UserProfileDTO(createdUserProfile)
        }
    }

    @PostMapping
    fun create(@RequestBody userProfileDto: UserProfileDTO) : UserProfileDTO{
        val userProfile = userProfileService.create(userProfileDto)
        return UserProfileDTO(userProfile)
    }

    @PutMapping
    fun update(@RequestBody userProfileDto: UserProfileDTO): UserProfileDTO{
        val userProfile = userProfileService.update(userProfileDto)
        return UserProfileDTO(userProfile)
    }

    @DeleteMapping
    fun delete(authentication: Authentication) {
        val userProfile = userProfileService.getByKeycloakId(authentication.name)
        userProfile?.userId?.let { userProfileService.delete(it) }
    }


    @PostMapping("/addPhoto")
    fun addPhoto(@RequestPart("photo") photo: MultipartFile, authentication: Authentication): Long? {
        val userProfile = userProfileService.getByKeycloakId(authentication.name)
        return userProfile?.userId?.let { userProfileService.addPhoto(it, photo) }
    }

    @GetMapping("/getPhoto")
    fun getPhoto(authentication: Authentication): ResponseEntity<ByteArray> {
        return try {
            val userProfile = userProfileService.getByKeycloakId(authentication.name)
            val photo: ByteArray? = userProfile?.userId?.let { userProfileService.getPhoto(it) }
            if (photo != null) {
                ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(photo)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/isUsernameTaken")
    fun isUsernameTaken(@RequestParam username: String, authentication: Authentication): ResponseEntity<Boolean> {
        val userProfile = userProfileService.getByKeycloakId(authentication.name)
        val currentUserId: Long? = userProfile?.userId
        val isTaken = userProfileService.isUsernameTaken(username, currentUserId)
        return ResponseEntity.ok(isTaken)
    }



}

