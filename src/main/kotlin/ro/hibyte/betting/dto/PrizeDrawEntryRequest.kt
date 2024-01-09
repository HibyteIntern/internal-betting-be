package ro.hibyte.betting.dto

data class PrizeDrawEntryRequest(
    val userId: Long,
    val prizeDrawId: Long,
    val amount: Number
)
