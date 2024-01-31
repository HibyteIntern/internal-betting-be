package ro.hibyte.betting.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ro.hibyte.betting.dto.CreateLeaderboardRequest
import ro.hibyte.betting.dto.LeaderboardDTO
import ro.hibyte.betting.dto.LeaderboardRequest
import ro.hibyte.betting.service.LeaderboardService

@RestController
@RequestMapping("/appi/v1/leaderboards")
class LeaderboardController(
    private val leaderboardService: LeaderboardService
) {

    @PostMapping
    fun createLeaderboard(request: CreateLeaderboardRequest) {
        leaderboardService.createLeaderboard(request)
    }

    @PutMapping
    fun getLeaderboard(@RequestBody leaderboardRequest: LeaderboardRequest): LeaderboardDTO =
        leaderboardService.computeLeaderboard(leaderboardRequest)
}
