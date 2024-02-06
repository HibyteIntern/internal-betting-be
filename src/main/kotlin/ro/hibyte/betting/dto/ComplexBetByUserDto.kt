package ro.hibyte.betting.dto

data class ComplexBetByUserDto(
    val user: UserProfileDTO? = null,
    val amount: Number,
    val odds: Double,
    val value: String,
    val betType: CompleteBetTypeDto
)
