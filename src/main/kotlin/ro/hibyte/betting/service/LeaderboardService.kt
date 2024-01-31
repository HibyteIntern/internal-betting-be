package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.*
import ro.hibyte.betting.entity.Leaderboard
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.metrics.FewestLosses
import ro.hibyte.betting.metrics.HighestEarner
import ro.hibyte.betting.metrics.MostBetsMetric
import ro.hibyte.betting.metrics.MostWinsMetric
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.LeaderboardRepository
import ro.hibyte.betting.repository.UserProfileRepository
import java.util.*

@Service
class LeaderboardService(
    private val leaderboardRepository: LeaderboardRepository,
    private val eventRepository: EventRepository,
    private val userProfileRepository: UserProfileRepository
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

        // compute leaderboard according to metrics from request

        val specifiedMetric = when (leaderboardRequest.sortedBy.lowercase(Locale.getDefault())) {
            "mostbets" -> MostBetsMetric()
            "mostwins" -> MostWinsMetric()
            "fewestlosses" -> FewestLosses()
            "highestearner" -> HighestEarner()
            else -> null
        }

        val leaderboardUsers = specifiedMetric?.calculateRank(users, events).orEmpty().map { user ->
            UserProfileDTO(userId = user.userId, username = user.username, bets = user.bets?.map { BetDTO(it) }?.toMutableList())
        }.toSet()

        return LeaderboardDTO(
            id = leaderboard.id,
            name = leaderboard.name,
            users = leaderboardUsers,
            // somehow the users with all their metrics scores and sorted by the desired metric
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
