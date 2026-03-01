package com.eosd.estudio_ancora.domain

import java.time.LocalDate

data class Day(
    val date: LocalDate,
    val timeSlots: List<TimeSlot>,
    val open: Boolean
)
