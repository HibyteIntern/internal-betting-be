package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class HighestAverageWinMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var totalCoinsWon = 0.0
        var totalWins = 0.0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{ bet ->
                if(bet.user?.userId == user.userId && bet.betType?.finalOutcome == bet.value) {
                    totalCoinsWon += findOddsValue(betType, bet) * bet.amount.toDouble()
                    totalWins++
                }
            }
        }
        if(totalWins == 0.0) return 0.0
        return totalCoinsWon / totalWins
    }
}