package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class HighestAverageBetMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var totalCoinsBet = 0.0
        var totalBets = 0.0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{ bet ->
                if(bet.user?.userId == user.userId) {
                    totalCoinsBet += bet.amount.toDouble()
                    totalBets++
                }
            }
        }
        return totalCoinsBet / totalBets
    }
}