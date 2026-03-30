package com.eosd.estudio_ancora.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DayTest {

    private val customer = Customer("John Doe", "123456789")
    private val service1Slot = Service("s1", "Corte", 1, 30.0)
    private val service2Slots = Service("s2", "Corte e Barba", 2, 50.0)
    
    private val date = LocalDate.of(2026, 3, 27)
    
    private val timeSlots = listOf(
        TimeSlot(LocalTime.of(8, 0), false, ""),
        TimeSlot(LocalTime.of(9, 0), false, ""),
        TimeSlot(LocalTime.of(10, 0), false, ""),
        TimeSlot(LocalTime.of(11, 0), false, "")
    )
    
    private val day = Day(date, timeSlots, true)

    @Test
    fun `booking a single slot service should update the correct timeslot`() {
        val booking = Booking("b1", customer, LocalDateTime.of(date, LocalTime.of(9, 0)), service1Slot)
        
        val updatedDay = day.bookTimeSlot(booking)
        
        val slot = updatedDay.timeSlots.find { it.hour == LocalTime.of(9, 0) }
        assertTrue(slot?.booked ?: false)
        assertEquals("b1", slot?.bookingId)
        
        // Verify other slots are still free
        updatedDay.timeSlots.filter { it.hour != LocalTime.of(9, 0) }.forEach {
            assertFalse(it.booked)
        }
    }

    @Test
    fun `booking a multi slot service should update multiple timeslots`() {
        val booking = Booking("b2", customer, LocalDateTime.of(date, LocalTime.of(9, 0)), service2Slots)
        
        val updatedDay = day.bookTimeSlot(booking)
        
        assertTrue(updatedDay.timeSlots.find { it.hour == LocalTime.of(9, 0) }?.booked ?: false)
        assertTrue(updatedDay.timeSlots.find { it.hour == LocalTime.of(10, 0) }?.booked ?: false)
        
        assertEquals("b2", updatedDay.timeSlots.find { it.hour == LocalTime.of(9, 0) }?.bookingId)
        assertEquals("b2", updatedDay.timeSlots.find { it.hour == LocalTime.of(10, 0) }?.bookingId)
        
        assertFalse(updatedDay.timeSlots.find { it.hour == LocalTime.of(8, 0) }?.booked ?: true)
        assertFalse(updatedDay.timeSlots.find { it.hour == LocalTime.of(11, 0) }?.booked ?: true)
    }

    @Test(expected = Exception::class)
    fun `booking an already booked slot should throw exception`() {
        val booking1 = Booking("b1", customer, LocalDateTime.of(date, LocalTime.of(9, 0)), service1Slot)
        val booking2 = Booking("b2", customer, LocalDateTime.of(date, LocalTime.of(9, 0)), service1Slot)
        
        val updatedDay = day.bookTimeSlot(booking1)
        updatedDay.bookTimeSlot(booking2)
    }

    @Test(expected = Exception::class)
    fun `booking a non-existent slot should throw exception`() {
        val booking = Booking("b1", customer, LocalDateTime.of(date, LocalTime.of(12, 0)), service1Slot)
        day.bookTimeSlot(booking)
    }

    @Test
    fun `unbooking should free the timeslots`() {
        val booking = Booking("b1", customer, LocalDateTime.of(date, LocalTime.of(9, 0)), service2Slots)
        val bookedDay = day.bookTimeSlot(booking)
        
        val unbookedDay = bookedDay.unbookTimeSlot(booking)
        
        unbookedDay.timeSlots.forEach {
            assertFalse(it.booked)
            assertEquals("", it.bookingId)
        }
    }
}
