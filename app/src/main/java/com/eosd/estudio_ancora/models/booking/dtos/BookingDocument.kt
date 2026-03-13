package com.eosd.estudio_ancora.models.booking.dtos

import androidx.annotation.Keep
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.models.service.ServiceDocument
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.time.ZoneId

@Keep
data class BookingDocument(
    @DocumentId
    val id: String = "undefined",
    val customer: CustomerDocument = CustomerDocument(),
    val dateTime: Timestamp = Timestamp(seconds = 0, nanoseconds = 0),
    val service: ServiceDocument = ServiceDocument()
) {
    fun toEntity(): Booking {
        return Booking(
            id = this.id,
            customer = customer.toEntity(),
            this.dateTime.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            service = this.service.toEntity()
        )
    }

    companion object {
        fun toDocument(booking: Booking): BookingDocument {
            val instant = booking.dateTime.atZone(ZoneId.systemDefault()).toInstant()
            val firebaseTimestamp = Timestamp(instant)

            return BookingDocument(
                id = booking.id,
                customer = CustomerDocument.toDocument(booking.customer),
                dateTime = firebaseTimestamp,
                service = ServiceDocument.toDocument(booking.service)
            )
        }
    }
}