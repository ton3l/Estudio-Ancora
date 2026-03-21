package com.eosd.estudio_ancora.services

import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.views.viewModels.states.AvailableTimesState
import java.time.LocalDate
import java.time.LocalDateTime

object DayService {
    suspend fun getDayAvailableTimes(day: LocalDate): AvailableTimesState {
        val dayEntity = DayModel.getDay(day)

        val dayAvailableTimes = if (dayEntity.open) dayEntity.timeSlots else emptyList()

        val availableTimes = AvailableTimesState.Success(
            availableTimes = dayAvailableTimes
                .filter { !it.booked }
                .filter { it.hour.atDate(day) > LocalDateTime.now() }
                .map { it.hour }
                .sorted()
        )

        return availableTimes
    }

    suspend fun fetchBookingDay(booking: Booking): Day { // IA: verificar se esse número de requisições é realmente necessário
        val date = booking.dateTime.toLocalDate()
        val bookingDay = DayModel.getBookingDay(date)
        if (bookingDay != null) return bookingDay

        DayModel.createBookingDay(date) // TODO implementar transaction nessas operações
        return DayModel.getBookingDay(date)!!
    }
}