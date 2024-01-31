package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.LeaderboardEntry
import ro.hibyte.betting.dto.MetricDTO
import ro.hibyte.betting.dto.UserProfileDTO
import ro.hibyte.betting.entity.Event
import ro.hibyte.betting.entity.UserProfile
import ro.hibyte.betting.metrics.*

@Service
class MetricService {
    fun computeLeaderboardEntryForUser(
        user: UserProfile,
        events: Set<Event>,
        metricNames: List<String>
    ): LeaderboardEntry {

        val metrics: MutableList<MetricDTO> = mutableListOf()
        metricNames.forEach{
            val computedMetric: MetricDTO? = computeMetricForUser(it, user, events)
            if(computedMetric != null) {
                metrics.add(computedMetric)
            }
        }
        return LeaderboardEntry(UserProfileDTO(user), metrics)
    }

    fun sortByMetric(metricName: String, leaderboardEntries: List<LeaderboardEntry>): List<LeaderboardEntry> {
        return leaderboardEntries.sortedByDescending { it.metrics.find { metric -> metric.metricName == metricName }?.computedValue }
    }

    private fun computeMetricForUser(metricName: String, user: UserProfile, events: Set<Event>): MetricDTO? {

        val baseStrategy: BaseMetric = when (metricName) {
            "wonBets" -> WonBetsMetric()
            "numberOfBets" -> NumberOfBetsMetric()
            "fewestLosses" -> FewestLossesMetric()
            "highestEarner" -> HighestEarnerMetric()
            "highestLoser" -> HighestLoserMetric()
            "largestSingleBet" -> LargestSingleBetMetric()
            "largestSingleWin" -> LargestSingleWinMetric()
            "largestSingleLoss" -> LargestSingleLossMetric()
            "winLossRatio" -> WLRatioMetric()
            "highestAverageBet" -> HighestAverageBetMetric()
            "highestAverageWin" -> HighestAverageWinMetric()
            else -> return null
        }

        return MetricDTO(metricName, baseStrategy.computeMetric(user, events))
    }
}