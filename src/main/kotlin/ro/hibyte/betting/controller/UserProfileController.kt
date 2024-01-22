package ro.hibyte.betting.controller


import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.FullUserProfileDto
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

    @GetMapping("/{userId}")
    fun getOne(@PathVariable userId: Long) : UserProfileDTO{
        val userProfile = userProfileService.get(userId)
        return UserProfileDTO(userProfile)
    }

    @GetMapping("/{userId}/full-dto")
    fun getOneFull(@PathVariable userId: Long) : FullUserProfileDto{
        val userProfile = userProfileService.get(userId)
        return FullUserProfileDto(userProfile)
    }

    @GetMapping("/byKeycloakId/{keycloakId}")
    fun getByKeycloakId(@PathVariable keycloakId: String): UserProfileDTO {
        val userProfile = userProfileService.getByKeycloakId(keycloakId)
        if (userProfile != null) {
            return UserProfileDTO(userProfile)
        } else {

            val newUserProfile = UserProfile()
            newUserProfile.keycloakId = keycloakId

            val createdUserProfile = userProfileService.create(UserProfileDTO(newUserProfile))

            return UserProfileDTO(createdUserProfile)
        }
    }

    @PostMapping
    fun create(@RequestBody userProfileDto: UserProfileDTO) : UserProfileDTO{
        val userProfile = userProfileService.create(userProfileDto)
        return UserProfileDTO(userProfile)
    }

    @PutMapping("/{userId}")
    fun update(@PathVariable userId: Long, @RequestBody userProfileDto: UserProfileDTO): UserProfileDTO{
        val userProfile = userProfileService.update(userProfileDto)
        return UserProfileDTO(userProfile)
    }

    @DeleteMapping("/{userId}")
    fun delete(@PathVariable userId: Long) = userProfileService.delete(userId)


    @PostMapping("/{userId}/addPhoto")
    fun addPhoto(@RequestPart("photo") photo: MultipartFile, @PathVariable userId: Long): Long? {
        return userProfileService.addPhoto(userId, photo)
    }

    @GetMapping("/{userId}/photo")
    fun getPhoto(@PathVariable userId: Long): ResponseEntity<ByteArray> {
        return try {
            val photo: ByteArray? = userProfileService.getPhoto(userId)
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



}


