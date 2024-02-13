package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.hibyte.betting.dto.FullUserGroupDTO
import ro.hibyte.betting.dto.UserGroupDTO
import ro.hibyte.betting.service.UserGroupService

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api/v1/user-groups")
class UserGroupController (private val userGroupService: UserGroupService){

    @GetMapping
    fun getAll(): List<FullUserGroupDTO> = userGroupService.getAll()

    @GetMapping("/{id}/full")
    fun getOneFull(@PathVariable id: Long): FullUserGroupDTO = userGroupService.getOneFull(id)

    @GetMapping("/{id}")
    fun getOne(@PathVariable id: Long): UserGroupDTO = userGroupService.getOne(id)

    @PostMapping
    fun create(@RequestBody userGroupDto: UserGroupDTO) : ResponseEntity<UserGroupDTO> =
        ResponseEntity(userGroupService.create(userGroupDto), HttpStatus.CREATED)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody userGroupDto: UserGroupDTO): UserGroupDTO =
        userGroupService.update(id, userGroupDto)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> =
        ResponseEntity(userGroupService.delete(id), HttpStatus.NO_CONTENT)

    @PostMapping("/{userGroupId}/addPhoto")
    fun addPhoto(@RequestPart("photo") photo: MultipartFile, @PathVariable userGroupId: Long): Long? =
        userGroupService.addPhoto(userGroupId, photo)

    @GetMapping("/{userGroupId}/photo")
    fun getPhoto(@PathVariable userGroupId: Long): ByteArray? =
        userGroupService.getPhoto(userGroupId)
}
