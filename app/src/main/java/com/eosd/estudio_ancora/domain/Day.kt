package com.eosd.estudio_ancora.domain

import java.time.LocalDate

data class Day(
    val date: LocalDate,
    val timeSlots: List<TimeSlot>,
    val open: Boolean
) {
    fun bookTimeSlot(booking: Booking): Day {
        val newTimeSlots = updateTimeSlots(booking) { slot ->
            if (slot.booked) throw Exception("Horário já reservado")
            slot.copy(booked = true, bookingId = booking.id)
        }

        return this.copy(timeSlots = newTimeSlots)
    }

    fun unbookTimeSlot(booking: Booking): Day {
        val newTimeSlots = updateTimeSlots(booking) {
            it.copy(booked = false, bookingId = "")
        }

        return this.copy(timeSlots = newTimeSlots)
    }

    fun updateTimeSlots(
        booking: Booking,
        transform: (timeSlot: TimeSlot) -> TimeSlot
    ): List<TimeSlot> {
        val targetTime = booking.dateTime.toLocalTime()
        var found = false

        val newTimeSlots = timeSlots.map { slot ->
            if (slot.hour == targetTime) {
                found = true
                transform(slot)
            }
            else
                slot
        }

        if (!found) throw Exception("Horário inexistente $targetTime")

        return newTimeSlots
    }
}
