package ro.hibyte.betting.dto

data class ComplexBetByUserDto(
    val userProfileDTO: UserProfileDTO,
    val betDTO: BetDTO
)
