package com.eosd.estudio_ancora.models.day.dtos

import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.domain.TimeSlot
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate
import java.time.LocalTime

data class WeekDayAvailableTimes(
    @DocumentId
    val weekDay: String = "undefined",
    val open: Boolean = false,
    val timeSlots: Map<String, Boolean> = emptyMap()
) {
    fun toDayEntity(date: LocalDate): Day {
        return Day(
            date = date,
            open = open,
            timeSlots = getTimeSlotEntities()
        )
    }

    fun toDayDocument(date: LocalDate): DayDocument {
        return DayDocument(
            open = open,
            date = date.toString(),
            timeSlots = getTimeSlotDocuments()
        )
    }

    private fun getTimeSlotEntities(): List<TimeSlot> {
        return timeSlots
            .filter { it.value } // is Available?
            .map { (hour) ->
                val time = LocalTime.parse(hour)

                TimeSlot(
                    hour = time,
                    booked = false,
                    bookingId = null
                )
            }
    }

    private fun getTimeSlotDocuments(): Map<String, TimeSlotDocument> {
        return timeSlots
            .filter { it.value } // is Available?
            .mapValues {
                TimeSlotDocument(
                    booked = false,
                    bookingId = ""
                )
            }
    }
}
