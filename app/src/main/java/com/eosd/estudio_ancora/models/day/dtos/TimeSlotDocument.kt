package com.eosd.estudio_ancora.models.day.dtos

import com.eosd.estudio_ancora.domain.TimeSlot
import java.time.LocalTime

data class TimeSlotDocument(
    val booked: Boolean = false,
    val bookingId: String = ""
) {
    fun toEntity(hour: String): TimeSlot {
        val time = LocalTime.parse(hour)

        return TimeSlot(
            hour = time,
            booked = booked,
            bookingId = bookingId.ifBlank { null }
        )
    }

    companion object {
        fun toDocument(timeSlot: TimeSlot): TimeSlotDocument {
            return TimeSlotDocument(
                booked = timeSlot.booked,
                bookingId = timeSlot.bookingId ?: ""
            )
        }
    }
}
