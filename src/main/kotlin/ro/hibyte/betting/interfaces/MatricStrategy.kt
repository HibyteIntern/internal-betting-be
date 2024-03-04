package ro.hibyte.betting.interfaces

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

interface MetricStrategy {
    fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Number>
}

class NumberOfBetsStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val betsCount = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val count = user.bets?.count { bet -> bet.betType?.event in events } ?: 0
            betsCount[user.userId!!] = count
        }

        return betsCount
    }
}

class MostWinsStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val winsCount = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val count = user.bets
                ?.filter { bet -> bet.betType?.event in events }
                ?.count { bet -> bet.value == bet.betType?.finalOutcome }
                ?: 0
            winsCount[user.userId!!] = count
        }

        return winsCount
    }
}

class FewestLossesStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val lossesCount = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val count = user.bets
                ?.filter { bet -> bet.betType?.event in events }
                ?.count { bet -> bet.value != bet.betType?.finalOutcome }
                ?: 0
            lossesCount[user.userId!!] = count
        }

        return lossesCount
    }
}

class HighestEarnerStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Double> {
        val earnings = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val totalEarnings = user.bets
                    ?.filter { bet -> bet.betType?.event in events && bet.value == bet.betType?.finalOutcome }
                    ?.sumOf { bet ->
                        val earnings = bet.amount.toDouble() * bet.odds
                        BigDecimal(earnings).setScale(2, RoundingMode.HALF_EVEN).toDouble()
                    } ?: 0.0
            earnings[user.userId!!] = BigDecimal(totalEarnings).setScale(2, RoundingMode.HALF_EVEN).toDouble()
        }

        return earnings
    }
}

class HighestLoserStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Double> {
        val losses = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val totalLosses = user.bets
                    ?.filter { bet -> bet.betType?.event in events && bet.value != bet.betType?.finalOutcome }
                    ?.sumOf { bet -> bet.amount?.toDouble() ?: 0.0 } ?: 0.0

            losses[user.userId!!] = totalLosses
        }
        return losses
    }
}

class LargestSingleBetStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Double> {
        val largestBets = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val maxBetAmount = user.bets
                ?.filter { bet -> bet.betType?.event in events }
                ?.maxOfOrNull { bet -> bet.amount.toDouble() }
                ?: 0.0
            largestBets[user.userId!!] = maxBetAmount
        }

        return largestBets
    }
}

class LargestSingleWinStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Double> {
        val largestWins = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val maxWinAmount = user.bets
                    ?.filter { bet -> bet.betType?.event in events && bet.value == bet.betType?.finalOutcome }
                    ?.maxOfOrNull { bet -> (bet.amount.toDouble() * bet.odds) }
                    ?.let { BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN).toDouble() }
                    ?: 0.0
            largestWins[user.userId!!] = maxWinAmount
        }

        return largestWins
    }
}

class LargestSingleLossStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Double> {
        val largestLosses = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val maxLossAmount = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value != bet.betType?.finalOutcome }
                ?.maxOfOrNull { bet -> bet.amount.toDouble() }
                ?: 0.0
            largestLosses[user.userId!!] = maxLossAmount
        }

        return largestLosses
    }
}

class WinLossRatioStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val winLossRatios = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val wins = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value == bet.betType?.finalOutcome }
                ?.count() ?: 0

            val losses = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value != bet.betType?.finalOutcome }
                ?.count() ?: 0

            val ratio = if (losses > 0) (wins.toDouble() / losses).roundToInt() else Int.MAX_VALUE
            winLossRatios[user.userId!!] = ratio
        }

        return winLossRatios
    }
}


class HighestAverageBetStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Double> {
        val averageBets = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val userBets = user.bets
                    ?.filter { bet -> bet.betType?.event in events }
                    ?: emptyList()

            val averageBet = if (userBets.isNotEmpty()) {
                val totalBetAmount = userBets.sumOf { bet -> bet.amount.toDouble() }
                BigDecimal(totalBetAmount / userBets.size).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            } else {
                0.0
            }

            averageBets[user.userId!!] = averageBet
        }

        return averageBets
    }
}

class HighestAverageWinStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Double> {
        val averageWins = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val userWins = user.bets
                    ?.filter { bet -> bet.betType?.event in events && bet.value == bet.betType?.finalOutcome }
                    ?: emptyList()

            val averageWin = if (userWins.isNotEmpty()) {
                val totalWinAmount = userWins.sumOf { bet -> (bet.amount.toDouble() * bet.odds) }
                BigDecimal(totalWinAmount / userWins.size).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            } else {
                0.0
            }

            averageWins[user.userId!!] = averageWin
        }

        return averageWins
    }
}

class LongestWinningStreakStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val longestWinningStreaks = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val bets = user.bets?.filter { bet -> bet.betType?.event in events }
            val longestStreak = calculateLongestWinningStreak(bets)

            longestWinningStreaks[user.userId!!] = longestStreak
        }

        return longestWinningStreaks
    }

    private fun calculateLongestWinningStreak(bets: List<Bet>?): Int {
        var currentStreak = 0 // Initialize the current streak length
        var longestStreak = 0 // Initialize the longest streak length

        for (bet in bets ?: emptyList()) {
            if (bet.value == bet.betType?.finalOutcome) {
                currentStreak++
                longestStreak = maxOf(longestStreak, currentStreak)
            } else {
                currentStreak = 0
            }
        }

        return longestStreak
    }
}











