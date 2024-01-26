package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.LeaderboardDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.UserProfileRepository
import java.sql.Timestamp


@Service
class LeaderboardService(
    private val eventRepository: EventRepository,
) {
    fun createLeaderboard(leaderboardDTO: LeaderboardDTO): LeaderboardDTO {
        val events = eventRepository.findByStartsAtGreaterThanEqualAndEndsAtLessThanEqual(
            Timestamp.from(leaderboardDTO.startDate),
            Timestamp.from(leaderboardDTO.endDate)
        )

        // Filter events to include only those with users in the leaderboard
        val filteredEvents = events.filter { event ->
            event.users.any { user -> leaderboardDTO.usersInLeaderboard?.contains(user.username) == true }
        }

        val leaderboardSorted = filteredEvents.flatMap { event ->
            event.users.map { user ->
                UserProfileDTO(username = user.username, userId = user.userId, keycloakId = user.keycloakId,
                    profilePicture = user.profilePicture, description = user.description, coins = user.coins
                )
            }
        }

        return LeaderboardDTO(
            startDate = leaderboardDTO.startDate,
            endDate = leaderboardDTO.endDate,
            usersInLeaderboard = leaderboardDTO.usersInLeaderboard,
            leaderboardSorted = leaderboardSorted
        )
    }
}
