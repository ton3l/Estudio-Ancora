package com.eosd.estudio_ancora.models.day.dtos

import com.eosd.estudio_ancora.domain.TimeSlot
import java.time.LocalTime

data class TimeSlotDocument(
    val booked: Boolean = false,
    val bookingId: String = "undefined"
) {
    fun toEntity(hour: String): TimeSlot {
        val time = LocalTime.of(hour.toInt(), 0)

        return TimeSlot(
            hour = time,
            booked = this.booked,
            bookingId = this.bookingId
        )
    }
}
