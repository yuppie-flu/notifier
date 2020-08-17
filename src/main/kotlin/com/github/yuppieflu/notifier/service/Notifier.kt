package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.domain.NotificationPackage

interface Notifier {
    fun notify(aPackage: NotificationPackage)
}
