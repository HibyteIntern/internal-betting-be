package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class LargestSingleLossMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var largestLossAmount = 0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{bet ->
                if(
                    bet.user?.userId == user.userId &&
                    bet.betType?.finalOutcome != bet.value &&
                    bet.amount.toInt() > largestLossAmount
                    ) {
                    largestLossAmount = bet.amount.toInt()
                }
            }
        }
        return largestLossAmount.toDouble()
    }
}