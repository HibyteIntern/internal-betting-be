package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class HighestLoserMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var lostCoins = 0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{bet ->
                if(bet.user?.userId == user.userId && bet.betType?.finalOutcome != bet.value)
                    lostCoins += bet.amount.toInt()
            }
        }
        return lostCoins.toDouble()
    }
}