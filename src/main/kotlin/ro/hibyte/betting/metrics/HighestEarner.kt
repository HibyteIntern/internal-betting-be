package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class HighestEarner:Metric{
    override fun calculateRank(users:Set<UserProfile>, events:Set<Event>):Set<UserProfile> {
        return users.sortedWith(compareByDescending<UserProfile> { it.coins.toDouble() }).toSet()
    }
}
