package com.eosd.estudio_ancora.models.booking

import androidx.annotation.Keep
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.models.service.ServiceDocument
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

@Keep
data class BookingDocument(
    @DocumentId
    val id: String = "",
    val customer: String = "",
    val dateTime: Timestamp = Timestamp(seconds = 0, nanoseconds = 0),
    val phoneNumber: String = "",
    val service: ServiceDocument = ServiceDocument()
) {
    fun toEntity(): Booking {
        return Booking(
            id = this.id,
            customer = this.customer,
            phoneNumber = this.phoneNumber,
            dateTime = this.dateTime.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
            service = this.service.toEntity()
        )
    }

    companion object {
        fun toDocument(booking: Booking): BookingDocument {
            val instant = booking.dateTime.atZone(ZoneId.systemDefault()).toInstant()
            val firebaseTimestamp = Timestamp(instant)

            return BookingDocument(
                id = booking.id,
                customer = booking.customer,
                dateTime = firebaseTimestamp,
                phoneNumber = booking.phoneNumber,
                service = ServiceDocument.toDocument(booking.service)
            )
        }
    }
}
