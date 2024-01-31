package ro.hibyte.betting.metrics

import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

class FewestLosses: Metric {
    override fun calculateRank(users: Set<UserProfile>, events: Set<Event>): Set<UserProfile> {
        return users.sortedBy { user -> user.bets?.filter { bet -> bet.value != bet.betType?.finalOutcome }?.count() }.toSet()
    }
}
