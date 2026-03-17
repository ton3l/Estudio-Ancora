package com.eosd.estudio_ancora.models.day.dtos

import com.eosd.estudio_ancora.domain.Day
import com.google.firebase.firestore.DocumentId
import java.time.LocalDate

data class DayDocument(
    @DocumentId
    val date: String = "undefined",
    val open: Boolean = false,
    val timeSlots: Map<String, TimeSlotDocument> = emptyMap()
) {
    fun toEntity(): Day {
        val entityTimeSlots = timeSlots.map { (hour, timeSlotDocument) ->
            timeSlotDocument.toEntity(hour)
        }

        return Day(
            date = LocalDate.parse(date),
            open = this.open,
            timeSlots = entityTimeSlots
        )
    }

    companion object {
        fun toDocument(day: Day): DayDocument {
            return DayDocument(
                date = day.date.toString(),
                open = day.open,
                timeSlots = day.timeSlots.associate { timeSlotEntity ->
                    timeSlotEntity.hour.toString() to TimeSlotDocument.toDocument(timeSlotEntity)
                }
            )
        }
    }
}
