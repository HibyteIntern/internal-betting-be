package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class WonBetsMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var wonBets = 0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{
                if(it.user?.userId == user.userId && it.betType?.finalOutcome == it.value) {
                    wonBets++
                }
            }
        }
        return wonBets.toDouble()
    }
}