package ro.hibyte.betting.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import ro.hibyte.betting.dto.ErrorResponse
import ro.hibyte.betting.exceptions.types.*

@ControllerAdvice
class AppAdvice {

    @ResponseBody
    @ExceptionHandler(value = [
        EntityNotFoundException::class
    ])
    fun entityNotFoundHandler(ex: RuntimeException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.NOT_FOUND)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ResponseBody
    @ExceptionHandler(value = [
        EntityAlreadyExistsException::class
    ])
    fun entityAlreadyExistsHandler(ex: EntityAlreadyExistsException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.CONFLICT)
        return ResponseEntity(errorResponse, HttpStatus.CONFLICT)
    }

    @ResponseBody
    @ExceptionHandler(value = [
        BadRequestException::class
    ])
    fun badRequestExceptionHandler(ex: RuntimeException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(ex.message ?: "error", HttpStatus.BAD_REQUEST)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
