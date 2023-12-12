package ro.hibyte.betting.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val status: HttpStatus,
    val time: LocalDateTime = LocalDateTime.now()
)
