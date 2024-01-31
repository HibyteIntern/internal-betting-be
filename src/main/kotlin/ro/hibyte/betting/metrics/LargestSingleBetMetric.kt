package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class LargestSingleBetMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var largestBetAmount = 0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{
                if(it.user?.userId == user.userId && it.amount.toInt() > largestBetAmount) {
                    largestBetAmount = it.amount.toInt()
                }
            }
        }
        return largestBetAmount.toDouble()
    }

}