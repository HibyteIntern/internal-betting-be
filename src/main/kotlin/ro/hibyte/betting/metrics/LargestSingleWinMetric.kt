package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Bet
import ro.hibyte.betting.entity.BetType
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class LargestSingleWinMetric: BaseMetric() {
    override fun computeMetric(user: UserProfile, events: Set<Event>): Double {
        var largestWinAmount = 0
        events.map{ it.betTypes }.forEach{ (betType) ->
            betType.bets.forEach{bet ->
                if(
                    bet.user?.userId == user.userId &&
                    bet.betType?.finalOutcome == bet.value &&
                    computeWinAmount(betType, bet) > largestWinAmount
                ) {
                    largestWinAmount = computeWinAmount(betType, bet)
                }
            }
        }
        return largestWinAmount.toDouble()
    }
    private fun computeWinAmount(betType: BetType, bet: Bet): Int {
        return (findOddsValue(betType, bet) * bet.amount.toInt()).toInt()
    }
}