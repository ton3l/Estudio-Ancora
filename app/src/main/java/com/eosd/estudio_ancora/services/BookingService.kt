package com.eosd.estudio_ancora.services

import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.models.booking.BookingModel
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.services.utils.generateFirestoreId
import com.eosd.estudio_ancora.views.interfaces.BookingInfo
import java.time.LocalDate

object BookingService {
    suspend fun addBooking(bookingInfo: BookingInfo) {
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
    }

    private suspend fun handleBookingDay(booking: Booking): Day { // IA: verificar se esse número de requisições é realmente necessário
        val date = booking.dateTime.toLocalDate()
        val bookingDay = DayModel.getBookingDay(date)
        if (bookingDay != null) return bookingDay

        DayModel.createBookingDay(date) // TODO implementar transaction nessas operações
        return DayModel.getBookingDay(date)!!
    }
}