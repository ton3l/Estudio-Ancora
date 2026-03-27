package com.eosd.estudio_ancora.domain

import java.time.LocalTime

data class TimeSlot(
    val hour: LocalTime,
    val booked: Boolean,
    val bookingId: String?
)
