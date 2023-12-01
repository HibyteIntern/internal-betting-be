package ro.hibyte.betting.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import ro.hibyte.betting.exceptions.types.BetTemplateNotFoundException
import ro.hibyte.betting.exceptions.types.BetTypeNotFoundException
import java.time.LocalDateTime

@ControllerAdvice
class AppAdvice {

    @ResponseBody
    @ExceptionHandler(value = [
        BetTemplateNotFoundException::class,
        BetTypeNotFoundException::class
    ])
    fun entityNotFoundHandler(ex: RuntimeException): ResponseEntity<Any> {
        val body: MutableMap<String, Any> = mutableMapOf()
        body["message"] = ex.message ?: "error"
        body["status"] = HttpStatus.NOT_FOUND
        body["time"] = LocalDateTime.now()

        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }
}