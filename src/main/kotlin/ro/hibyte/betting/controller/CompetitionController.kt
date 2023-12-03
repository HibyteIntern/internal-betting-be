package ro.hibyte.betting.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.CompetitionDto
import ro.hibyte.betting.service.CompetitionService

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
@RequestMapping("/api/competitions")
class CompetitionController {
    @Autowired
    private lateinit var competitionService: CompetitionService

    @GetMapping
    fun getAll(): List<CompetitionDto> {
        val competitions = competitionService.getAll()

        return competitions.map { CompetitionDto(it) }
    }

    @GetMapping("/{id}")
    fun getOneById(@PathVariable id: Long): CompetitionDto {
        val competition = competitionService.getOne(id)

        return CompetitionDto(competition)
    }

    @PostMapping
    fun create(@RequestBody dto:CompetitionDto): CompetitionDto {
        val competition = competitionService.create(dto)

        return CompetitionDto(competition)
    }
    // TODO: fix put causes error 500
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody dto: CompetitionDto): CompetitionDto {
        val competition = competitionService.update(dto)

        return CompetitionDto(competition)
    }

    @DeleteMapping("/{id}")
    fun deleteOne(@PathVariable id: Long) = competitionService.delete(id)
}