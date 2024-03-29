package com.rkcoding.taskreminder.todo_features.domain.model

import java.time.LocalDateTime

data class AlarmItem(
    val alarmTime: LocalDateTime,

    val message: String
)
