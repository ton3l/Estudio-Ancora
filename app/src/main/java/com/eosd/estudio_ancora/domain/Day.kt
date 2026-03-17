package com.eosd.estudio_ancora.domain

import java.time.LocalDate

data class Day(
    val date: LocalDate,
    val timeSlots: List<TimeSlot>,
    val open: Boolean
) {
    fun bookTimeSlot(booking: Booking): Day {
        val targetTime = booking.dateTime.toLocalTime()
        var targetSlot: TimeSlot? = null
        val newTimeSlots = timeSlots.map { slot ->
            if (slot.hour == targetTime && !slot.booked) {
                targetSlot = slot
                slot.copy(
                    booked = true,
                    bookingId = booking.id
                ) // TODO verificar disponibilidade de horário
            }
            else
                slot
        }

        targetSlot ?: throw Exception("Horário inexistente $targetTime")

        return this.copy(timeSlots = newTimeSlots)
    }
}
