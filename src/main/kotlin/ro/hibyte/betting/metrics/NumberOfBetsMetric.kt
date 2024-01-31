package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class NumberOfBetsMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var numberOfBets = 0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{
                if(it.user?.userId == user.userId) {
                    numberOfBets++
                }
            }
        }
        return numberOfBets.toDouble()
    }
}