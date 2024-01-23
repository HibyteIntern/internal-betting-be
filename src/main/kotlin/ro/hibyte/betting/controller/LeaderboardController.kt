package ro.hibyte.betting.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.LeaderboardDTO
import ro.hibyte.betting.service.LeaderboardService

@RestController
@RequestMapping("/api/v1/leaderboards")
@CrossOrigin(origins = ["http://localhost:4200"])
class LeaderboardController(private val leaderboardService: LeaderboardService) {
    @GetMapping("/get")
    fun getLeaderboard(@RequestBody leaderboardDTO: LeaderboardDTO):ResponseEntity<LeaderboardDTO>{
        val result = leaderboardService.createLeaderboard(leaderboardDTO)
        return ResponseEntity(result,HttpStatus.OK)
    }
}
