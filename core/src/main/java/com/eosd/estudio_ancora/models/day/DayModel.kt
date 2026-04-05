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

    fun getDayRef(date: LocalDate) = bookingDaysCollection.document(date.toString())
    fun getWeekAvailableRef(date: LocalDate) = weekAvailableTimesCollection.document(date.dayOfWeek.toString().lowercase())

    suspend fun getDay(date: LocalDate): Day{
        val dbDay = bookingDaysCollection
            .document(date.toString())
            .get()
            .await()

        if( dbDay.exists() ) {
            val day = dbDay
                .toObject<DayDocument>()!!
                .toEntity()

            return day
        }

        val day = weekAvailableTimesCollection
            .document(date.dayOfWeek.toString().lowercase())
            .get()
            .await()
            .toObject<WeekDayAvailableTimes>()!!
            .toDayEntity(date)

        return day
    }

    suspend fun getBookingDay(date: LocalDate): Day?{
        val dbDay = bookingDaysCollection
            .document(date.toString())
            .get()
            .await()

        return if( dbDay.exists() )
            dbDay
            .toObject<DayDocument>()!!
            .toEntity()
        else null
    }

    suspend fun updateDay(day: Day){
        val dayDocument = DayDocument.toDocument(day)
        bookingDaysCollection
            .document(dayDocument.date)
            .set(dayDocument) // TODO Bug fix
            .await()
    }

    suspend fun getAllWeekDayAvailableTimes(): List<WeekDayAvailableTimes> {
        val snapshot = weekAvailableTimesCollection.get().await()
        return snapshot.toObjects(WeekDayAvailableTimes::class.java)
    }

    suspend fun updateWeekDayAvailableTime(weekDay: WeekDayAvailableTimes) {
        weekAvailableTimesCollection
            .document(weekDay.weekDay)
            .set(weekDay)
            .await()
    }
}