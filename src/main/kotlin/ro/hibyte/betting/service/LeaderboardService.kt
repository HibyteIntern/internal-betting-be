package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.CreateLeaderboardRequest
import ro.hibyte.betting.dto.LeaderboardDTO
import ro.hibyte.betting.dto.LeaderboardRequest
import ro.hibyte.betting.entity.Leaderboard
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.LeaderboardRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class LeaderboardService(
    private val leaderboardRepository: LeaderboardRepository,
    private val eventRepository: EventRepository,
    private val userProfileRepository: UserProfileRepository
) {

    fun createLeaderboard(request: CreateLeaderboardRequest) {
        val leaderboard = Leaderboard(
            name = request.name,
            events = if (request.events.isNotEmpty()) eventRepository.findAllByEventIdIn(request.events).toSet()
            else eventRepository.findAll().toSet(),
            userProfiles = if (request.userProfiles.isNotEmpty()) userProfileRepository.findAllByUserIdIn(request.userProfiles)
                .toSet()
            else userProfileRepository.findAll().toSet()
        )
        leaderboardRepository.save(leaderboard)
    }

    fun computeLeaderboard(leaderboardRequest: LeaderboardRequest): LeaderboardDTO {
        val leaderboard = leaderboardRepository.findById(leaderboardRequest.leaderboardId)
            .orElseThrow { EntityNotFoundException("Leaderboard", leaderboardRequest.leaderboardId) }
        val events = leaderboard.events
        val users = leaderboard.userProfiles

        // compute leaderboard according to metrics from request

        return LeaderboardDTO(
            id = leaderboard.id!!,
            name = leaderboard.name,
            // somehow the users with all their metrics scores and sorted by the desired metric
        )
    }
}
