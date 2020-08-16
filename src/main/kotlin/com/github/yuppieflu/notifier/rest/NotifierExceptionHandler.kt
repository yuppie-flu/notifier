package com.github.yuppieflu.notifier.rest

import com.github.yuppieflu.notifier.UserNotFoundException
import com.github.yuppieflu.notifier.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class NotifierExceptionHandler {

    private val log = LoggerFactory.getLogger(NotifierExceptionHandler::class.java)

    @ExceptionHandler(Throwable::class)
    fun exceptionHandler(th: Throwable): ResponseEntity<ErrorDto> =
        when (th) {
            is UserNotFoundException -> ResponseEntity<ErrorDto>(
                ErrorDto("No users with id [${th.userId}]"), HttpStatus.NOT_FOUND
            )
            is ValidationException -> ResponseEntity<ErrorDto>(
                ErrorDto(th.message ?: "Validation error"), HttpStatus.BAD_REQUEST
            )
            else -> {
                log.error("unexpected exception", th)
                ResponseEntity<ErrorDto>(ErrorDto("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
}

data class ErrorDto(val error: String)
