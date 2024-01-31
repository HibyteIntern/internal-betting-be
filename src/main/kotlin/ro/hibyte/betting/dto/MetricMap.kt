package ro.hibyte.betting.dto

data class MetricMap(
    val userId: Long,
    val metrics: Map<String, Int>
)
