package com.github.yuppieflu.notifier

import java.util.UUID

class UserNotFoundException(val userId: UUID) :
    RuntimeException("No users with id [$userId]")

open class ValidationException(message: String) : RuntimeException(message)

class InvalidTimezoneException(timezone: String) :
    ValidationException("Invalid timezone [$timezone]")

class InternalServerException(cause: Throwable) : Exception(cause)
