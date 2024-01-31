package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.LeaderboardConfig
import ro.hibyte.betting.dto.LeaderboardDTO
import ro.hibyte.betting.dto.LeaderboardRequest
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.Leaderboard
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.LeaderboardRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class LeaderboardService(
    private val leaderboardRepository: LeaderboardRepository,
    private val eventRepository: EventRepository,
    private val userProfileRepository: UserProfileRepository,
    private val computeMetricsService: ComputeMetricsService
) {

    fun createLeaderboard(request: LeaderboardConfig): LeaderboardConfig {
        val leaderboard = Leaderboard(
            name = request.name,
            events = if (request.events.isNotEmpty()) eventRepository.findAllByEventIdIn(request.events).toSet()
            else eventRepository.findAll().toSet(),
            userProfiles = if (request.userProfiles.isNotEmpty()) userProfileRepository.findAllByUserIdIn(request.userProfiles)
                .toSet()
            else userProfileRepository.findAll().toSet()
        )
        return leaderboardRepository.save(leaderboard).let {
            LeaderboardConfig(
                id = it.id,
                name = it.name,
                events = it.events.map { event -> event.eventId },
                userProfiles = it.userProfiles.map { userProfile -> userProfile.userId!! }
            )
        }
    }

    private fun computeLeaderboardMetrics(metrics: List<String> ,events: Set<Event>, users: Set<UserProfile>): Map<String, List<UserProfileDTO>> {
        val metricResults = mutableMapOf<String, List<UserProfileDTO>>()

        for (metric in metrics) {
            val result = when (metric) {
                "Most Bets" -> computeMetricsService.usersWithMostBets(users)
                "Most Wins" -> computeMetricsService.usersWithMostWins(users)
                "Fewest Losses" -> computeMetricsService.usersWithFewestLosses(users)
                "Highest Earner" -> computeMetricsService.usersHighestEarners(users)
                "Highest Looser" -> computeMetricsService.usersLowestEarners(users)
                "Largest Single Bet" -> computeMetricsService.usersLargestBets(users)
                "Largest Single Win" -> computeMetricsService.usersSortedByLargestSingleWin(users)
                "Largest Single Loss" -> computeMetricsService.usersSortedByLargestSingleLoss(users)
                "Win/Loss Ratio" -> computeMetricsService.usersSortedByWinLossRatio(users)
                else -> emptyList()
            }

            metricResults[metric] = result
        }

        return metricResults
    }

    fun computeLeaderboard(leaderboardRequest: LeaderboardRequest): LeaderboardDTO {
        val leaderboard = leaderboardRepository.findById(leaderboardRequest.leaderboardId)
            .orElseThrow { EntityNotFoundException("Leaderboard", leaderboardRequest.leaderboardId) }
        val events = leaderboard.events
        val users = leaderboard.userProfiles

        val metricsResults = computeLeaderboardMetrics(leaderboardRequest.metrics,events,users)

        return LeaderboardDTO(
            id = leaderboard.id,
            name = leaderboard.name,
            metricsResults = metricsResults
        )
    }

    fun getAllLeaderboardConfigs(): List<LeaderboardConfig> {
        return leaderboardRepository.findAll().map {
            LeaderboardConfig(
                id = it.id,
                name = it.name,
                events = it.events.map { event -> event.eventId },
                userProfiles = it.userProfiles.map { userProfile -> userProfile.userId!! }
            )
        }
    }

    @Transactional
    fun deleteLeaderboard(id: Long) {
        leaderboardRepository.deleteById(id)
    }
}
