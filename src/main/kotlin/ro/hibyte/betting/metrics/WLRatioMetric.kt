package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class WLRatioMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var wins = 0.0
        var losses = 0.0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{ bet ->
                if(bet.user?.userId == user.userId) {
                    if( bet.betType?.finalOutcome == bet.value ) wins++
                    else losses++
                }
            }
        }
        return wins / losses
    }
}