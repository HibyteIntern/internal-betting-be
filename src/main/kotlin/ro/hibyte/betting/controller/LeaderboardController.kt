package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ro.hibyte.betting.dto.LeaderboardConfig
import ro.hibyte.betting.dto.LeaderboardDTO
import ro.hibyte.betting.dto.LeaderboardRequest
import ro.hibyte.betting.service.LeaderboardService

@RestController
@RequestMapping("/api/v1/leaderboards")
@CrossOrigin(origins = ["http://localhost:4200"])
class LeaderboardController(
    private val leaderboardService: LeaderboardService
) {
    @PostMapping
    fun createLeaderboard(@RequestBody request: LeaderboardConfig): ResponseEntity<LeaderboardConfig> {
        leaderboardService.createLeaderboard(request)
        return ResponseEntity(request, HttpStatus.CREATED)
    }

    @PutMapping
    fun getLeaderboard(@RequestBody leaderboardRequest: LeaderboardRequest): LeaderboardDTO =
        leaderboardService.computeLeaderboard(leaderboardRequest)

    @GetMapping
    fun getAllLeaderboards(): List<LeaderboardConfig> =
        leaderboardService.getAllLeaderboardConfigs()

    @DeleteMapping("/{id}")
    fun deleteLeaderboard(@PathVariable id: Long): ResponseEntity<Void> {
        leaderboardService.deleteLeaderboard(id)
        return ResponseEntity.noContent().build()
    }
}
