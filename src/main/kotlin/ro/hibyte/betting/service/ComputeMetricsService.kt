package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.LeaderboardRepository
import ro.hibyte.betting.repository.UserProfileRepository
@Service
class ComputeMetricsService(
    private val eventRepository: EventRepository,
    private val userProfileRepository: UserProfileRepository
) {
    val allUsers: List<UserProfile> = userProfileRepository.findAll()

    

    private fun calculateWinLossRatio(wins: Int, losses: Int): Double {
        if (losses == 0) {
            return Double.POSITIVE_INFINITY
        }

        return wins.toDouble() / losses.toDouble()
    }

    fun usersWithMostBets(): List<UserProfileDTO> {
        val sortedUsers = allUsers.sortedBy { user ->
            user.bets?.size ?: 0
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }

    fun usersWithFewestLosses(): List<UserProfileDTO> {
        val sortedUsers = allUsers.sortedBy { user ->
            user.bets?.count { bet ->
                bet.betType?.finalOutcome != bet.value
            } ?: 0
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,

                coins = userProfile.coins
            )
        }
    }
    fun usersWithMostWins(): List<UserProfileDTO> {
        val sortedUsers = allUsers.sortedBy { user ->
            user.bets?.count { bet ->
                bet.betType?.finalOutcome == bet.value
            } ?: 0
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }

    fun usersLowestEarners(): List<UserProfileDTO> {
        val sortedUsers = allUsers.sortedByDescending { user ->
            user.coins.toDouble()
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }

    fun usersHighestEarners(): List<UserProfileDTO> {
        val sortedUsers = allUsers.sortedBy { user ->
            user.coins.toDouble()
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }

    fun usersLargestBets(): List<UserProfileDTO> {

        val sortedUsers = allUsers.sortedBy { user ->
            user.bets?.maxOfOrNull { bet ->
                bet.amount.toDouble()
            } ?: 0.0
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }
    fun usersSortedByLargestSingleWin(): List<UserProfileDTO> {

        val sortedUsers = allUsers.sortedBy { user ->
            user.bets?.filter { bet ->
                bet.betType?.finalOutcome == bet.value
            }?.maxOfOrNull { bet ->
                bet.amount.toDouble()
            } ?: 0.0
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }

    fun usersSortedByLargestSingleLoss(): List<UserProfileDTO> {

        val sortedUsers = allUsers.sortedBy { user ->
            user.bets?.filter { bet ->
                bet.betType?.finalOutcome != bet.value
            }?.maxOfOrNull { bet ->
                bet.amount.toDouble()
            } ?: 0.0
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }

    fun usersSortedByWinLossRatio(): List<UserProfileDTO> {
        val allUsers = userProfileRepository.findAll()

        val sortedUsers = allUsers.sortedByDescending { user ->
            val wins = user.bets?.count { bet ->
                bet.betType?.finalOutcome == bet.value
            } ?: 0

            val losses = user.bets?.count { bet ->
                bet.betType?.finalOutcome != bet.value
            } ?: 0

            calculateWinLossRatio(wins, losses)
        }

        return sortedUsers.map { userProfile ->
            UserProfileDTO(
                userId = userProfile.userId,
                keycloakId = userProfile.keycloakId,
                username = userProfile.username,
                profilePicture = userProfile.profilePicture,
                description = userProfile.description,
                coins = userProfile.coins
            )
        }
    }
}
