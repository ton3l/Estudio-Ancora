package com.eosd.estudio_ancora.models.booking

import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.booking.dtos.BookingDocument
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

object BookingModel {
    private val bookingCollection = firestore.collection("bookings")

    suspend fun createBooking (booking: Booking): DocumentReference {
        val data = BookingDocument.toDocument(booking)

        val document = bookingCollection
            .add(data)
            .await()

        return document
    }
}