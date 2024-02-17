package com.rkcoding.taskreminder.core.utils

import android.os.Build
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long?.toDateFormat(): String{
    val date: LocalDate = this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant
                .ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    } ?: LocalDate.now()

    return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}