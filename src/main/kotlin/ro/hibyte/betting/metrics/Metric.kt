package ro.hibyte.betting.metrics

import ro.hibyte.betting.dto.MetricMap
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile

interface Metric {
    fun calculateRank(users: Set<UserProfile>, events: Set<Event>): Set<UserProfile>
}
