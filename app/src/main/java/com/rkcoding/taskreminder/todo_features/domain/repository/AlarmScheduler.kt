package com.rkcoding.taskreminder.todo_features.domain.repository

import com.rkcoding.taskreminder.todo_features.domain.model.AlarmItem

interface AlarmScheduler {

    fun schedule(alarmItem: AlarmItem)

    fun cancel(alarmItem: AlarmItem)


}