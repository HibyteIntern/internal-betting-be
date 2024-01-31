package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class ComputeMetricsService(
    private val eventRepository: EventRepository,
    private val userProfileRepository: UserProfileRepository
) {

    private fun calculateWinLossRatio(wins: Int, losses: Int): Double {
        if (losses == 0) {
            return Double.POSITIVE_INFINITY
        }

        return wins.toDouble() / losses.toDouble()
    }

    fun usersWithMostBets(profiles: Set<UserProfile>): List<UserProfileDTO> {
        val sortedUsers = profiles.sortedBy { user ->
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

    fun usersWithFewestLosses(profiles: Set<UserProfile>): List<UserProfileDTO> {
        val sortedUsers = profiles.sortedBy { user ->
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

    fun usersWithMostWins(profiles: Set<UserProfile>): List<UserProfileDTO> {
        val sortedUsers = profiles.sortedBy { user ->
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

    fun usersLowestEarners(profiles: Set<UserProfile>): List<UserProfileDTO> {
        val sortedUsers = profiles.sortedByDescending { user ->
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

    fun usersHighestEarners(profiles: Set<UserProfile>): List<UserProfileDTO> {
        val sortedUsers = profiles.sortedBy { user ->
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

    fun usersLargestBets(profiles: Set<UserProfile>): List<UserProfileDTO> {

        val sortedUsers = profiles.sortedBy { user ->
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

    fun usersSortedByLargestSingleWin(profiles: Set<UserProfile>): List<UserProfileDTO> {

        val sortedUsers = profiles.sortedBy { user ->
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

    fun usersSortedByLargestSingleLoss(profiles: Set<UserProfile>): List<UserProfileDTO> {

        val sortedUsers = profiles.sortedBy { user ->
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

    fun usersSortedByWinLossRatio(profiles: Set<UserProfile>): List<UserProfileDTO> {
        val sortedUsers = profiles.sortedByDescending { user ->
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
