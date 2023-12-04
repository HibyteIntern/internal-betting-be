package ro.hibyte.betting.controller


import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.service.UserProfileService


@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/user-profiles")
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

}