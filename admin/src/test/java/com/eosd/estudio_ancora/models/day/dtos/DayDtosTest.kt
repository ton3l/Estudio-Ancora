package com.eosd.estudio_ancora.models.day.dtos

import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.domain.TimeSlot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class DayDtosTest {

    @Test
    fun timeSlotDocument_mapping_isCorrect() {
        val hour = "09:00"
        val entity = TimeSlot(hour = LocalTime.parse(hour), booked = true, bookingId = "b1")
        
        val document = TimeSlotDocument.toDocument(entity)
        assertTrue(document.booked)
        assertEquals("b1", document.bookingId)
        
        val mappedBack = document.toEntity(hour)
        assertEquals(entity, mappedBack)
    }

    @Test
    fun timeSlotDocument_mapping_emptyBookingId_isCorrect() {
        val hour = "10:00"
        val entity = TimeSlot(hour = LocalTime.parse(hour), booked = false, bookingId = null)
        
        val document = TimeSlotDocument.toDocument(entity)
        assertFalse(document.booked)
        assertEquals("", document.bookingId)
        
        val mappedBack = document.toEntity(hour)
        assertEquals(entity, mappedBack)
    }

    @Test
    fun dayDocument_mapping_isCorrect() {
        val date = LocalDate.of(2026, 4, 4)
        val entity = Day(
            date = date,
            open = true,
            timeSlots = listOf(
                TimeSlot(hour = LocalTime.of(9, 0), booked = false),
                TimeSlot(hour = LocalTime.of(10, 0), booked = true, bookingId = "id1")
            )
        )
        
        val document = DayDocument.toDocument(entity)
        assertEquals(date.toString(), document.date)
        assertTrue(document.open)
        assertEquals(2, document.timeSlots.size)
        assertTrue(document.timeSlots.containsKey("09:00"))
        assertTrue(document.timeSlots.containsKey("10:00"))
        
        val mappedBack = document.toEntity()
        assertEquals(entity.date, mappedBack.date)
        assertEquals(entity.open, mappedBack.open)
        assertEquals(entity.timeSlots.size, mappedBack.timeSlots.size)
        // Check sorting and contents
        assertEquals(LocalTime.of(9, 0), mappedBack.timeSlots[0].hour)
        assertEquals(LocalTime.of(10, 0), mappedBack.timeSlots[1].hour)
    }

    @Test
    fun weekDayAvailableTimes_toEntity_isCorrect() {
        val date = LocalDate.of(2026, 4, 6) // A Monday
        val weekDay = WeekDayAvailableTimes(
            weekDay = "monday",
            open = true,
            timeSlots = mapOf("09:00" to true, "10:00" to false, "11:00" to true)
        )
        
        val entity = weekDay.toDayEntity(date)
        assertEquals(date, entity.date)
        assertTrue(entity.open)
        assertEquals(2, entity.timeSlots.size) // Only 09:00 and 11:00 are true
        assertEquals(LocalTime.of(9, 0), entity.timeSlots[0].hour)
        assertEquals(LocalTime.of(11, 0), entity.timeSlots[1].hour)
    }
}
