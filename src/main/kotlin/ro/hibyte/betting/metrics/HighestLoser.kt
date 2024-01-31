package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class HighestLoser: Metric {
    override fun calculateRank(users: Set<UserProfile>, events: Set<Event>): Set<UserProfile> {
        return users.sortedWith(compareBy<UserProfile> { it.coins.toDouble() }).toSet()
    }
}
