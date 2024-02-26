package ro.hibyte.betting.controller


import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.UserProfile
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
        return if (userProfile != null) {
            UserProfileDTO(
                    userId = userProfile.userId,
                    keycloakId = userProfile.keycloakId,
                    username = userProfile.username,
                    profilePicture = userProfile.profilePicture,
                    description = userProfile.description,
                    coins = userProfile.coins,
                    groups = userProfile.groups?.map { it.userGroupId }?.toMutableSet()
            )
        } else {
            val newUserProfile = UserProfile()
            newUserProfile.keycloakId = authentication.name
            val createdUserProfile = userProfileService.create(UserProfileDTO(newUserProfile))
            UserProfileDTO(createdUserProfile)
        }
    }

    @PostMapping
    fun create(@RequestBody userProfileDto: UserProfileDTO) : UserProfileDTO{
        val userProfile = userProfileService.create(userProfileDto)
        return UserProfileDTO(userProfile)
    }

    @PostMapping("/many")
    fun createMany(@RequestBody userProfilesDto: List<UserProfileDTO>) : List<UserProfileDTO>{
        val userProfiles = userProfilesDto.map { userProfileService.create(it) }
        return userProfiles.map { UserProfileDTO(it) }
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

    @GetMapping("/getPhoto/{userId}")
    fun getPhotoById(@PathVariable userId: Long): ResponseEntity<ByteArray> {
        return try {
            val userProfile = userProfileService.get(userId)
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
    fun isUsernameTaken(
            @RequestParam username: String,
            @RequestParam(required = false) currentUsername: String?
    ): ResponseEntity<Boolean> {
        val isTaken = if (currentUsername != null && currentUsername == username) {
            false
        } else {
            userProfileService.isUsernameTaken(username)
        }
        return ResponseEntity.ok(isTaken)
    }

}

