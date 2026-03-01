package com.eosd.estudio_ancora.models.booking

import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.libs.firestore
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

object BookingModel {
    suspend fun addBooking (booking: Booking): DocumentReference {
        val data = BookingDocument.toDocument(booking)

        val document = firestore.collection("bookings")
            .add(data)
            .await()

        return document
    }
}