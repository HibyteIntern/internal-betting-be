package ro.hibyte.betting.controller

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.UserGroupDto
import ro.hibyte.betting.entity.UserGroup
import ro.hibyte.betting.service.UserGroupService

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api/user-groups")
class UserGroupController (private val userGroupService: UserGroupService){

    @GetMapping
    fun getAll(): List<UserGroupDto> =
        userGroupService.getAll().map{ UserGroupDto(it) }

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): UserGroupDto =
        UserGroupDto(userGroupService.getOne(id))

    @PostMapping
    fun create(@RequestBody userGroupDto: UserGroupDto) =
        UserGroupDto(userGroupService.create(userGroupDto))

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody userGroupDto: UserGroupDto): UserGroupDto {
        val userGroup = userGroupService.update(id, userGroupDto)
        return UserGroupDto(userGroup)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = userGroupService.delete(id)

    @PostMapping("/{userGroupId}/addPhoto")
    fun addPhoto(@RequestPart("photo") photo: MultipartFile, @PathVariable userGroupId: Long): Long? {
        return userGroupService.addPhoto(userGroupId, photo)
    }
}
