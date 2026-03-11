package com.eosd.estudio_ancora.services

import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.views.viewModels.states.AvailableTimesState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object DayService {
    suspend fun getDayAvailableTimes(day: LocalDate): AvailableTimesState {
        val timeSlots = DayModel.getDayTimeSlots(day)
        val availableTimes = AvailableTimesState.Success(
            availableTimes = timeSlots
                .filter { !it.booked }
                .filter { it.hour.atDate(day) > LocalDateTime.now() }
                .map { it.hour }
                .sorted()
        )

        return availableTimes
    }
}