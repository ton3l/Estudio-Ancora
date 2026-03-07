package com.eosd.estudio_ancora.models.day

import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.domain.TimeSlot
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.day.dtos.DayDocument
import com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

object DayModel {
    private val bookingDaysCollection = firestore.collection("booking-days")
    private val weekAvailableTimesCollection = firestore.collection("week-available-times")

    suspend fun getDayTimeSlots(date: LocalDate): List<TimeSlot>{
        val dbDay = bookingDaysCollection
            .document(date.toString())
            .get()
            .await()

        if( dbDay.exists() ) {
            val dayDocument = dbDay.toObject<DayDocument>()!!
            val day = dayDocument.toEntity()

            if ( !day.open ) return emptyList()

            val dayAvailableTimes = day.component2()
            return dayAvailableTimes
        }

        val weekDay = weekAvailableTimesCollection
            .document(date.dayOfWeek.toString().lowercase())
            .get()
            .await()
            .toObject<WeekDayAvailableTimes>()!!

        if ( !weekDay.open ) return emptyList()

        return weekDay.getTimeSlotEntities()
    }
}