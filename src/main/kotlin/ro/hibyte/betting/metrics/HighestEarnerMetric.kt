package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class HighestEarnerMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var coinsEarned = 0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{ bet ->
                if(bet.user?.userId == user.userId && bet.betType?.finalOutcome == bet.value) {
                    val odds = findOddsValue(betType, bet)
                    coinsEarned += (bet.amount.toInt() * odds).toInt()
                }
            }
        }
        return coinsEarned.toDouble()
    }
}