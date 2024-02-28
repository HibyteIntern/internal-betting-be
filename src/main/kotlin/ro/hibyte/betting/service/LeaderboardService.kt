package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.LeaderboardConfig
import ro.hibyte.betting.dto.LeaderboardDTO
import ro.hibyte.betting.dto.LeaderboardRequest
import ro.hibyte.betting.entity.Leaderboard
import ro.hibyte.betting.entity.LeaderboardEntry
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.interfaces.*
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.LeaderboardRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class LeaderboardService(
    private val leaderboardRepository: LeaderboardRepository,
    private val eventRepository: EventRepository,
    private val userProfileRepository: UserProfileRepository,
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


    fun computeLeaderboard(leaderboardRequest: LeaderboardRequest): LeaderboardDTO {
        val leaderboard = leaderboardRepository.findById(leaderboardRequest.leaderboardId)
            .orElseThrow { EntityNotFoundException("Leaderboard", leaderboardRequest.leaderboardId) }
        val events = leaderboard.events
        val users = leaderboard.userProfiles

        val metricStrategies: Map<String, MetricStrategy> = mapOf(
            "numberOfBets" to NumberOfBetsStrategy(),
            "mostWins" to MostWinsStrategy(),
            "fewestLosses" to FewestLossesStrategy(),
            "highestEarner" to HighestEarnerStrategy(),
            "highestLoser" to HighestLoserStrategy(),
            "largestSingleBet" to LargestSingleBetStrategy(),
            "largestSingleWin" to LargestSingleWinStrategy(),
            "largestSingleLoss" to LargestSingleLossStrategy(),
            "win/lossRatio" to WinLossRatioStrategy(),
            "highestAverageBet" to HighestAverageBetStrategy(),
            "highestAverageWin" to HighestAverageWinStrategy(),
            "longestWinningStreakStrategy" to LongestWinningStreakStrategy(),
        )

        val resultMap = mutableMapOf<Long, MutableMap<String, Number>>()

        leaderboardRequest.metrics.forEach { metric ->
            val strategy = metricStrategies[metric] ?: throw IllegalArgumentException("Invalid metric: $metric")
            val metricResult = strategy.compute(users, events)
            metricResult.forEach { (userId, value) ->
                val userMetrics = resultMap.getOrPut(userId) { mutableMapOf() }
                userMetrics[metric] = value
            }
        }

        val sortedResults = resultMap.entries.sortedByDescending {
            it.value[leaderboardRequest.sortedBy]?.toDouble() ?: 0.0
        }


        val leaderboardEntries = sortedResults.map { (userId, metrics) ->
            LeaderboardEntry(userId, metrics.mapValues { it.value })
        }

        return LeaderboardDTO(
            id = leaderboard.id,
            name = leaderboard.name,
            entries = leaderboardEntries
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
