package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class MostBetsMetric : Metric {
    override fun calculateRank(users: Set<UserProfile>, events: Set<Event>): Set<UserProfile> {
        return users.sortedBy { user -> user.bets?.size ?: 0 }.reversed().toSet()
    }
}
