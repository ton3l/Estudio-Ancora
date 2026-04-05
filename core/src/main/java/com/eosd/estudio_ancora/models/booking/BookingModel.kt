package com.eosd.estudio_ancora.models.booking

import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.booking.dtos.BookingDocument
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.Timestamp
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

object BookingModel {
    private val bookingCollection = firestore.collection("bookings")

    fun getBookingRef(bookingId: String) = bookingCollection.document(bookingId)

    suspend fun createBooking (booking: Booking) {
        bookingCollection
            .document(booking.id)
            .set(BookingDocument.toDocument(booking))
            .await()
    }

    suspend fun getBooking(bookingId: String): Booking {
        return bookingCollection
            .document(bookingId)
            .get()
            .await()
            .toObject<BookingDocument>()!!
            .toEntity()
    }

    suspend fun getBookings(bookingIds: List<String>): List<Booking> {
        if (bookingIds.isEmpty()) return emptyList()
        val bookingDocs = bookingCollection
            .whereIn(FieldPath.documentId(), bookingIds)
            .get()
            .await()
            .toObjects<BookingDocument>()

        return bookingDocs.map { it.toEntity() }
    }

    suspend fun getAllFutureBookings(): List<Booking> {
        val now = Timestamp.now()
        val bookingDocs = bookingCollection
            .whereGreaterThanOrEqualTo("dateTime", now)
            .orderBy("dateTime", Query.Direction.ASCENDING)
            .get()
            .await()
            .toObjects<BookingDocument>()

        return bookingDocs.map { it.toEntity() }
    }

    suspend fun deleteBooking(booking: Booking) {
        bookingCollection
            .document(booking.id)
            .delete()
            .await()
    }
}