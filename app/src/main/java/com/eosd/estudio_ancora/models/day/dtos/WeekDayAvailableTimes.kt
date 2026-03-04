package com.eosd.estudio_ancora.models.day.dtos

import com.eosd.estudio_ancora.domain.TimeSlot
import com.google.firebase.firestore.DocumentId
import java.time.LocalTime

data class WeekDayAvailableTimes(
    @DocumentId
    val weekDay: String = "undefined",
    val open: Boolean = false,
    val timeSlots: Map<String, Boolean> = emptyMap()
) {
    fun getTimeSlotEntities(): List<TimeSlot> {
        return this.timeSlots.map { (hour, booked) ->
            val time = LocalTime.of(hour.toInt(), 0)

            TimeSlot(
                hour = time,
                booked = booked,
                bookingId = "undefined"
            )
        }
    }
}
