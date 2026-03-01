package com.eosd.estudio_ancora.domain

data class TimeSlot(
    val hour: Number,
    val booked: Boolean,
    val bookingId: String
)
