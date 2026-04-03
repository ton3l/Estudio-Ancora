package com.eosd.estudio_ancora.services

import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.states.AvailableTimesState
import java.time.LocalDate
import java.time.LocalDateTime

object DayService {
    suspend fun getDayAvailableTimes(day: LocalDate): AvailableTimesState {
        val dayEntity = DayModel.getDay(day)

        val dayAvailableTimes = if (dayEntity.open) dayEntity.timeSlots else emptyList()

        val sortedTimes = dayAvailableTimes.sortedBy { it.hour }
        
        val availableTimesMap = sortedTimes.associate { timeSlot ->
            val isAvailable = !timeSlot.booked && timeSlot.hour.atDate(day) > LocalDateTime.now()
            timeSlot.hour to isAvailable
        }

        return AvailableTimesState.Success(
            availableTimes = availableTimesMap
        )
    }
}