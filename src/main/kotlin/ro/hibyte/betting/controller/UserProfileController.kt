package ro.hibyte.betting.controller


import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.BetDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.service.BetService
import ro.hibyte.betting.service.UserProfileService


@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/user-profiles")
class UserProfileController(private val userProfileService: UserProfileService, private val betService: BetService) {

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


}
