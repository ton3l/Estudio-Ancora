package com.eosd.estudio_ancora.services

import android.content.Context
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.booking.BookingLocalPersistence
import com.eosd.estudio_ancora.models.booking.BookingModel
import com.eosd.estudio_ancora.models.booking.dtos.BookingDocument
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.models.day.dtos.DayDocument
import com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes
import com.eosd.estudio_ancora.services.utils.generateFirestoreId
import com.eosd.estudio_ancora.states.ActiveBookingsState
import com.eosd.estudio_ancora.states.BookingFormState
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime

object BookingService {
    suspend fun addBooking(context: Context, bookingInfo: BookingFormState) {
        val booking = Booking(
            id = generateFirestoreId(),
            customer = bookingInfo.customer,
            dateTime = bookingInfo.dateTime,
            service = bookingInfo.service!!
        )

        firestore.runTransaction { transaction ->
            val date = booking.dateTime.toLocalDate()
            val dayRef = DayModel.getDayRef(date)
            val daySnapshot = transaction.get(dayRef)

            val dayEntity: Day = if (daySnapshot.exists()) {
                daySnapshot.toObject<DayDocument>()!!.toEntity()
            } else {
                val weekRef = DayModel.getWeekAvailableRef(date)
                val weekSnapshot = transaction.get(weekRef)
                weekSnapshot.toObject<WeekDayAvailableTimes>()!!.toDayEntity(date)
            }

            val updatedDay = dayEntity.bookTimeSlot(booking)

            transaction.set(dayRef, DayDocument.toDocument(updatedDay))
            transaction.set(BookingModel.getBookingRef(booking.id), BookingDocument.toDocument(booking))

            null
        }.await()

        BookingLocalPersistence.addBookingId(context, booking.id)
    }

    suspend fun getUserActiveBookings(context: Context): ActiveBookingsState {
        val ids = BookingLocalPersistence.getBookingIds(context).first()
        if (ids.isEmpty()) return ActiveBookingsState.Success(emptyList())

        val bookings = BookingModel.getBookings(ids.toList())
        val (pastBookings, activeBookings) = bookings.partition { it.dateTime < LocalDateTime.now() }

        pastBookings.forEach { BookingLocalPersistence.removeBookingId(context, it.id) }

        return ActiveBookingsState.Success(activeBookings.sortedBy { it.dateTime })
    }

    suspend fun getAllFutureBookings(): List<Booking> {
        return BookingModel.getAllFutureBookings()
    }

    suspend fun deleteBookingByAdmin(booking: Booking) {
        firestore.runTransaction { transaction ->
            val date = booking.dateTime.toLocalDate()
            val dayRef = DayModel.getDayRef(date)
            val daySnapshot = transaction.get(dayRef)

            if (!daySnapshot.exists()) {
                throw IllegalStateException("Cannot delete booking: Day document does not exist.")
            }

            val dayEntity = daySnapshot.toObject<DayDocument>()!!.toEntity()
            val updatedDay = dayEntity.unbookTimeSlot(booking)

            transaction.set(dayRef, DayDocument.toDocument(updatedDay))
            transaction.delete(BookingModel.getBookingRef(booking.id))

            null
        }.await()
    }

    suspend fun deleteBooking(context: Context, booking: Booking) {
        firestore.runTransaction { transaction ->
            val date = booking.dateTime.toLocalDate()
            val dayRef = DayModel.getDayRef(date)
            val daySnapshot = transaction.get(dayRef)

            if (!daySnapshot.exists()) {
                throw IllegalStateException("Cannot delete booking: Day document does not exist.")
            }

            val dayEntity = daySnapshot.toObject<DayDocument>()!!.toEntity()
            val updatedDay = dayEntity.unbookTimeSlot(booking)

            transaction.set(dayRef, DayDocument.toDocument(updatedDay))
            transaction.delete(BookingModel.getBookingRef(booking.id))

            null
        }.await()

        BookingLocalPersistence.removeBookingId(context, booking.id)
    }
}