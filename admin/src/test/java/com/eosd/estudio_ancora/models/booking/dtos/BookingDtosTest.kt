package com.eosd.estudio_ancora.models.booking.dtos

import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.google.firebase.Timestamp
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class BookingDtosTest {

    @Test
    fun customerDocument_mapping_isCorrect() {
        val entity = Customer(name = "Test", phoneNumber = "123")
        val document = CustomerDocument.toDocument(entity)
        
        assertEquals(entity.name, document.name)
        assertEquals(entity.phoneNumber, document.phoneNumber)
        
        val mappedBack = document.toEntity()
        assertEquals(entity, mappedBack)
    }

    @Test
    fun bookingDocument_mapping_isCorrect() {
        val ldt = LocalDateTime.of(2026, 4, 4, 10, 0)
        val entity = Booking(
            id = "b1",
            customer = Customer(name = "C1", phoneNumber = "P1"),
            dateTime = ldt,
            service = Service(id = "s1", name = "S1", price = 10.0, duration = 1)
        )
        
        val document = BookingDocument.toDocument(entity)
        assertEquals(entity.id, document.id)
        assertEquals(entity.customer.name, document.customer.name)
        
        // Timestamp conversion check
        val expectedTimestamp = Timestamp(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()))
        assertEquals(expectedTimestamp.seconds, document.dateTime.seconds)
        
        val mappedBack = document.toEntity()
        assertEquals(entity.id, mappedBack.id)
        assertEquals(entity.dateTime, mappedBack.dateTime)
        assertEquals(entity.customer.name, mappedBack.customer.name)
        assertEquals(entity.service.name, mappedBack.service.name)
    }
}
