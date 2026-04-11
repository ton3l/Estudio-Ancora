package com.eosd.estudio_ancora.services

import com.eosd.estudio_ancora.models.booking.BookingModel
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes
import com.eosd.estudio_ancora.states.AvailableTimesState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

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

    suspend fun getWeekRules(): List<com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes> {
        return DayModel.getAllWeekDayAvailableTimes()
    }

    suspend fun updateWeekRule(weekDay: com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes) {
        DayModel.updateWeekDayAvailableTime(weekDay)
    }

    suspend fun updateCurrentWeekSchedules() {
        val today = LocalDate.now()
        val sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

        val weekRules = getWeekRules()

        generateSequence(today) { it.plusDays(1) }
            .takeWhile { !it.isAfter(sunday) }
            .forEach { currentDate ->
                val dayName = currentDate.dayOfWeek.toString().lowercase()
                val rule = weekRules.find { it.weekDay == dayName } ?: return@forEach
                updateDaySchedule(currentDate, rule)
            }
    }

    private suspend fun updateDaySchedule(
        date: LocalDate,
        rule: WeekDayAvailableTimes
    ) {
        val existingDay = DayModel.getBookingDay(date) ?: return

        val newDayEntity = rule.toDayEntity(date)
        val oldSlots = existingDay.timeSlots

        if (!newDayEntity.open) {
            // Day is closed, all old booked slots must be deleted
            oldSlots.filter { it.booked }.forEach { slot ->
                slot.bookingId?.let { BookingModel.deleteBookingById(it) }
            }
            DayModel.updateDay(newDayEntity.copy(timeSlots = emptyList()))
            return
        }

        val newSlotsByHour = newDayEntity.timeSlots.associateBy { it.hour }
        val mergedSlots = newDayEntity.timeSlots.map { newSlot ->
            val oldSlot = oldSlots.find { it.hour == newSlot.hour }
            if (oldSlot != null && oldSlot.booked) {
                return@map newSlot.copy(booked = true, bookingId = oldSlot.bookingId)
            }
            newSlot
        }

        // Delete bookings for slots that were removed
        oldSlots.forEach { oldSlot ->
            if (oldSlot.booked && !newSlotsByHour.containsKey(oldSlot.hour)) {
                oldSlot.bookingId?.let { BookingModel.deleteBookingById(it) }
            }
        }

        DayModel.updateDay(newDayEntity.copy(timeSlots = mergedSlots))
    }
}