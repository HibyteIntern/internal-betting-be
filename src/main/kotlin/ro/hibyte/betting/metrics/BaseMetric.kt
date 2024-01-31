package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

abstract class BaseMetric {
    abstract fun computeMetric(user: UserProfile, events: Set<Event>): Double

    //helper methods for calculating metrics
    protected fun findOddsValue(betType: BetType, bet: Bet): Double {
        betType.betTemplate.multipleChoiceOptions.forEachIndexed(){ index, option ->
            if(option == bet.value) {
                return betType.odds[index]
            }
        }
        return 0.0
    }
}