package com.eosd.estudio_ancora.services

import android.content.Context
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.models.booking.BookingLocalPersistence
import com.eosd.estudio_ancora.models.booking.BookingModel
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.services.utils.generateFirestoreId
import com.eosd.estudio_ancora.views.viewModels.states.ActiveBookingsState
import com.eosd.estudio_ancora.views.viewModels.states.BookingFormState
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

object BookingService {
    suspend fun addBooking(context: Context, bookingInfo: BookingFormState) {
        val booking = Booking(
            id = generateFirestoreId(),
            customer = bookingInfo.customer,
            dateTime = bookingInfo.dateTime,
            service = bookingInfo.service!!
        )
        val bookingDay = DayService.fetchBookingDay(booking)
        val updatedDay = bookingDay.bookTimeSlot(booking)
        DayModel.updateDay(updatedDay)
        BookingModel.createBooking(booking)
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

    suspend fun deleteBooking(context: Context, booking: Booking) {
        val bookingDay = DayService.fetchBookingDay(booking)
        val updatedDay = bookingDay.unbookTimeSlot(booking)
        DayModel.updateDay(updatedDay)
        BookingModel.deleteBooking(booking)
        BookingLocalPersistence.removeBookingId(context, booking.id)
    }
}