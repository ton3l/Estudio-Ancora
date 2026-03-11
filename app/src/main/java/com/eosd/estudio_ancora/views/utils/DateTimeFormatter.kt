package com.eosd.estudio_ancora.views.utils

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.toPtBrSplitText(): Pair<String, String> {
    val dateFormatter = DateTimeFormatter.ofPattern(
        "EEE dd 'de' MMMM 'de' yyyy",
        Locale("pt", "BR")
    )
    val dateText = this.format(dateFormatter)
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale(
                    "pt",
                    "BR"
                )
            ) else it.toString()
        }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("pt", "BR"))
    val timeText = this.format(timeFormatter)

    return Pair(dateText, timeText)
}

fun LocalTime.toHHmm(): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("pt", "BR"))
    val timeText = this.format(timeFormatter)

    return timeText
}