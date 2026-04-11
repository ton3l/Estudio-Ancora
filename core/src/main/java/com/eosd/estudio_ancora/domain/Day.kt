package com.eosd.estudio_ancora.domain

import java.time.LocalDate

data class Day(
    val date: LocalDate,
    val timeSlots: List<TimeSlot>,
    val open: Boolean
) {
    fun bookTimeSlot(booking: Booking): Day {
        val targetTime = booking.dateTime.toLocalTime()
        val duration = booking.service.duration
        val startIndex = timeSlots.indexOfFirst { it.hour == targetTime }
        val durationOutOfBoundsErrorMessage = "Duração do serviço '${booking.service.name}' é maior do que a quantidade de horários disponíveis em sequência."

        if (startIndex == -1) {
            throw Exception("Horário inexistente $targetTime")
        }

        if (startIndex + duration > timeSlots.size) {
            throw Exception(durationOutOfBoundsErrorMessage)
        }

        val affectedSlots = timeSlots.subList(startIndex, startIndex + duration)

        if (affectedSlots.any { it.booked }) {
            throw Exception("Horário atual ou dentro da duração do serviço '${booking.service.name}' já está reservado.")
        }

        val isContinuous = affectedSlots.zipWithNext { slot, nextSlot ->
            slot.hour.plusHours(1) == nextSlot.hour
        }.all { it }

        if (!isContinuous) {
            throw Exception(durationOutOfBoundsErrorMessage)
        }

        val newTimeSlots = updateTimeSlots(booking) { slot ->
            slot.copy(booked = true, bookingId = booking.id)
        }

        return this.copy(timeSlots = newTimeSlots)
    }

    fun unbookTimeSlot(booking: Booking): Day {
        val newTimeSlots = updateTimeSlots(booking) { slot ->
            slot.copy(booked = false, bookingId = "")
        }

        return this.copy(timeSlots = newTimeSlots)
    }

    fun updateTimeSlots(
        booking: Booking,
        transform: (timeSlot: TimeSlot) -> TimeSlot
    ): List<TimeSlot> {
        val targetTime = booking.dateTime.toLocalTime()
        val duration = booking.service.duration

        val startIndex = timeSlots.indexOfFirst { it.hour == targetTime }
        if (startIndex == -1) throw Exception("Horário inexistente $targetTime")

        return timeSlots.mapIndexed { index, slot ->
            if (index in startIndex until (startIndex + duration)) {
                return@mapIndexed transform(slot)
            }
            slot
        }
    }
}
