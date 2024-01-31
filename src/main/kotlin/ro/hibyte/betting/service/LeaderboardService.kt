package ro.hibyte.betting.service

import jakarta.transaction.Transactional
import org.apache.catalina.User
import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.*
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.entity.Leaderboard
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.repository.BetTypeRepository
import ro.hibyte.betting.repository.EventRepository
import ro.hibyte.betting.repository.LeaderboardRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class LeaderboardService(
    private val leaderboardRepository: LeaderboardRepository,
    private val eventRepository: EventRepository,
    private val userProfileRepository: UserProfileRepository,
    private val betTypeRepository: BetTypeRepository
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

        val betTypes = betTypeRepository.findAll().toSet()

        val results: MutableList<List<UserResult>> = mutableListOf()

        // compute leaderboard according to metrics from request
        leaderboardRequest.metrics.forEach{ metric ->
            when (metric) {
                "numberOfBets" -> results.add(getLeaderboardMostBets(users, betTypes, leaderboardRequest.sortedBy))
                "wonBets" -> results.add(getLeaderboardMostWins(users, betTypes, leaderboardRequest.sortedBy))
                "fewestLosses" -> results.add(getLeaderboardFewestLosses(users, betTypes, leaderboardRequest.sortedBy))
                //"highestEarner" -> results.add(getLeaderboardHighestEarner(users, betTypes, leaderboardRequest.sortedBy))
            }

        }

        return LeaderboardDTO(
            id = leaderboard.id,
            name = leaderboard.name,
            leaderboardResults = results.toList()
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

    fun getLeaderboardMostBets(users: Set<UserProfile>, betTypes: Set<BetType>, sortedBy: String): List<UserResult> {
        return users.sortedWith(
            compareByDescending<UserProfile> { usersByMostBetsComparator(it) }
                .thenBy { pickComparator(sortedBy, it, betTypes) }
            ).map { user ->
                UserResult(
                    user = UserProfileDTO(user),
                    score = user.bets?.size ?: 0
                )
            }.toList()
    }

    fun getLeaderboardMostWins(users: Set<UserProfile>, betTypes: Set<BetType>, sortedBy: String): List<UserResult> {
        return users.sortedWith(
            compareByDescending<UserProfile> { usersByLeaderboardMostWinsComparator(it, betTypes) }
            .thenBy { pickComparator(sortedBy, it, betTypes) }
        )
            .map{ it -> UserResult(
                user = UserProfileDTO(it),
                score =  usersByMostBetsComparator(it)
            )}
            .toList()
    }

    fun getLeaderboardFewestLosses(users: Set<UserProfile>, betTypes: Set<BetType>, sortedBy: String): List<UserResult> {
        return users.sortedWith(
            compareByDescending<UserProfile> { usersByFewestLossesComparator(it, betTypes) }
                .thenBy { pickComparator(sortedBy, it, betTypes) }
        )
            .map{ it -> UserResult(
                user = UserProfileDTO(it),
                score =  usersByMostBetsComparator(it)
            )}
            .toList()
    }

//    fun getLeaderboardHighestEarner(users: Set<UserProfile>, betTypes: Set<BetType>, sortedBy: String): List<UserResult> {
//        return users.sortedWith(
//            compareByDescending<UserProfile> { usersByHighestEarnerComparator(it, betTypes) }
//                .thenBy { pickComparator(sortedBy, it, betTypes) }
//        )
//            .map{ it -> UserResult(
//                user = UserProfileDTO(it),
//                score =  usersByMostBetsComparator(it)
//            )}
//            .toList()
//    }

    fun pickComparator(sortedBy: String, user: UserProfile, betTypes: Set<BetType>): Int {
        return when (sortedBy) {
            "numberOfBets" -> usersByMostBetsComparator(user)
            "wonBets" -> usersByLeaderboardMostWinsComparator(user, betTypes)
            "fewestLosses" -> usersByFewestLossesComparator(user, betTypes)
            //"highestEarner" -> usersByHighestEa
            else -> 0
        }
    }

    fun usersByMostBetsComparator(user: UserProfile): Int {
        return user.bets?.size ?: 0
    }

    fun usersByLeaderboardMostWinsComparator(user: UserProfile, betTypes: Set<BetType>): Int {
        return user.bets?.count { bet ->
            bet.value == betTypes.find { it.id == bet.betType?.id }?.finalOutcome
        } ?: 0
    }

    fun usersByFewestLossesComparator(user: UserProfile, betTypes: Set<BetType>): Int {
        return user.bets?.count { bet ->
            bet.value != betTypes.find { it.id == bet.betType?.id }?.finalOutcome
        } ?: 0
    }

//    fun usersByHighestEarnerComparator(user: UserProfile, betTypes: Set<BetType>): Int {
//        return user.bets?.sumOf { bet ->
//            if (bet.value == betTypes.find { it.id == bet.betType?.id }?.finalOutcome) {
//                bet.amount * bet.odds
//            } else {
//                0
//            }
//        }?.toInt() ?: 0
//    }
}


