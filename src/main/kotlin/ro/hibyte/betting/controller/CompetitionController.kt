package ro.hibyte.betting.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.CompetitionDTO
import ro.hibyte.betting.dto.CompetitionRequest
import ro.hibyte.betting.mapper.CompetitionMapper
import ro.hibyte.betting.service.CompetitionService

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/v1/competitions")
class CompetitionController {
    @Autowired
    private lateinit var competitionService: CompetitionService

    @Autowired
    private lateinit var competitionMapper: CompetitionMapper

    @GetMapping
    fun getAll(@RequestParam name: String?): List<CompetitionDTO> =
        name?.let { competitionService.searchByName(name) } ?: competitionService.getAll()

    @GetMapping("/{id}")
    fun getOneById(@PathVariable id: Long): CompetitionDTO = competitionMapper.mapCompetitionToCompetitionDto(competitionService.getOne(id))

    @PostMapping
    fun create(@RequestBody dto: CompetitionRequest): ResponseEntity<CompetitionDTO> = ResponseEntity(competitionMapper.mapCompetitionToCompetitionDto(competitionService.create(dto)), HttpStatus.CREATED)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CompetitionRequest): ResponseEntity<CompetitionDTO> = ResponseEntity(competitionMapper.mapCompetitionToCompetitionDto(competitionService.update(id, dto)), HttpStatus.OK)

    @DeleteMapping("/{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<Void> {
        competitionService.delete(id)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
