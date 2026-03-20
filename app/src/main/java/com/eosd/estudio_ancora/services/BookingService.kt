package com.eosd.estudio_ancora.services

import android.content.Context
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.models.booking.BookingLocalPersistence
import com.eosd.estudio_ancora.models.booking.BookingModel
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.services.utils.generateFirestoreId
import com.eosd.estudio_ancora.views.interfaces.BookingInfo
import com.eosd.estudio_ancora.views.viewModels.states.ActiveBookingsState
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime

object BookingService {
    suspend fun addBooking(context: Context, bookingInfo: BookingInfo) {
        val booking = Booking(
            id = generateFirestoreId(),
            customer = bookingInfo.customer,
            dateTime = bookingInfo.dateTime,
            service = bookingInfo.service
        )
        val bookingDay = handleBookingDay(booking)
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

    private suspend fun handleBookingDay(booking: Booking): Day { // IA: verificar se esse número de requisições é realmente necessário
        val date = booking.dateTime.toLocalDate()
        val bookingDay = DayModel.getBookingDay(date)
        if (bookingDay != null) return bookingDay

        DayModel.createBookingDay(date) // TODO implementar transaction nessas operações
        return DayModel.getBookingDay(date)!!
    }
}