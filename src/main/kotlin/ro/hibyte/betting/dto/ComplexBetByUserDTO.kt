package ro.hibyte.betting.dto

data class ComplexBetByUserDTO(
    val userProfileDTO: UserProfileDTO,
    val betDTO: BetDTO
)
