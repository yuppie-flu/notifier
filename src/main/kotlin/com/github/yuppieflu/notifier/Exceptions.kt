package com.github.yuppieflu.notifier

import java.util.UUID

class UserNotFoundException(userId: UUID) :
    Exception("No users with id [$userId]")

class DbAccessException() : Exception()
