package ro.hibyte.betting.interfaces

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import kotlin.math.roundToInt

interface MetricStrategy {
    fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int>
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
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val earnings = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val totalEarnings = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value == bet.betType?.finalOutcome }
                ?.sumOf { bet -> (bet.amount.toDouble() * bet.odds).toInt() }
                ?: 0
            earnings[user.userId!!] = totalEarnings
        }

        return earnings
    }
}

class HighestLoserStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val losses = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val totalLosses = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value != bet.betType?.finalOutcome }
                ?.sumOf { bet -> bet.amount.toDouble() }
                ?.toInt()
                ?: 0
            losses[user.userId!!] = totalLosses
        }

        return losses
    }
}

class LargestSingleBetStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val largestBets = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val maxBetAmount = user.bets
                ?.filter { bet -> bet.betType?.event in events }
                ?.maxOfOrNull { bet -> bet.amount.toDouble() }
                ?: 0.0
            largestBets[user.userId!!] = maxBetAmount.roundToInt()
        }

        return largestBets
    }
}


class LargestSingleWinStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val largestWins = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val maxWinAmount = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value == bet.betType?.finalOutcome }
                ?.maxOfOrNull { bet -> (bet.amount.toDouble() * bet.odds).roundToInt() }
                ?: 0
            largestWins[user.userId!!] = maxWinAmount
        }

        return largestWins
    }
}

class LargestSingleLossStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val largestLosses = mutableMapOf<Long, Int>()

        users.forEach { user ->
            val maxLossAmount = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value != bet.betType?.finalOutcome }
                ?.maxOfOrNull { bet -> bet.amount.toDouble().roundToInt() }
                ?: 0
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
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val averageBets = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val userBets = user.bets
                ?.filter { bet -> bet.betType?.event in events }
                ?: emptyList()

            val averageBet = if (userBets.isNotEmpty()) {
                val totalBetAmount = userBets.sumByDouble { bet -> bet.amount.toDouble() }
                totalBetAmount / userBets.size
            } else {
                0.0
            }

            averageBets[user.userId!!] = averageBet
        }

        val highestAverageBetUser = averageBets.maxByOrNull { it.value }

        val result = mutableMapOf<Long, Int>()
        if (highestAverageBetUser != null) {
            result[highestAverageBetUser.key] = highestAverageBetUser.value.roundToInt()
        }

        return result
    }
}

class HighestAverageWinStrategy : MetricStrategy {
    override fun compute(users: Set<UserProfile>, events: Set<Event>): Map<Long, Int> {
        val averageWins = mutableMapOf<Long, Double>()

        users.forEach { user ->
            val userWins = user.bets
                ?.filter { bet -> bet.betType?.event in events && bet.value == bet.betType?.finalOutcome }
                ?: emptyList()

            val averageWin = if (userWins.isNotEmpty()) {
                val totalWinAmount = userWins.sumByDouble { bet -> (bet.amount.toDouble() * bet.odds) }
                totalWinAmount / userWins.size
            } else {
                0.0
            }

            averageWins[user.userId!!] = averageWin
        }

        val highestAverageWinUser = averageWins.maxByOrNull { it.value }

        val result = mutableMapOf<Long, Int>()
        if (highestAverageWinUser != null) {
            result[highestAverageWinUser.key] = highestAverageWinUser.value.roundToInt()
        }

        return result
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











