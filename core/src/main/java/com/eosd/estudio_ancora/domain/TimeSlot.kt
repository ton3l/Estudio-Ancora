package com.eosd.estudio_ancora.domain

import androidx.annotation.Keep
import java.time.LocalTime

@Keep
data class TimeSlot(
    val hour: LocalTime,
    val booked: Boolean,
    val bookingId: String? = null
)
