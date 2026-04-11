package com.eosd.estudio_ancora.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.domain.*
import com.eosd.estudio_ancora.models.booking.BookingModel
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class UpdateWeekScheduleTest {

    private val today = LocalDate.now()
    private val weekdayName = today.dayOfWeek.toString().lowercase()
    private var originalRule: WeekDayAvailableTimes? = null
    
    private val bookingIdKeep = "test-booking-keep"
    private val bookingIdRemove = "test-booking-remove"

    @Before
    fun setUp() {
        runBlocking {
            // Save original rule to restore later
            originalRule = DayModel.getAllWeekDayAvailableTimes().find { it.weekDay == weekdayName }
            
            // Clean up any potential leftover from previous failed runs
            cleanUp()
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            cleanUp()
            
            // Restore original rule
            originalRule?.let {
                DayModel.updateWeekDayAvailableTime(it)
            }
        }
    }

    private suspend fun cleanUp() {
        if (DayModel.getBookingDay(today) != null) {
            com.eosd.estudio_ancora.libs.firestore.collection("booking-days").document(today.toString()).delete().await()
        }
        
        try { BookingModel.deleteBookingById(bookingIdKeep) } catch (e: Exception) {}
        try { BookingModel.deleteBookingById(bookingIdRemove) } catch (e: Exception) {}
    }

    @Test
    fun testUpdateCurrentWeekSchedules() = runBlocking {
        val testCustomer = Customer("Test Client", "123456789")
        val testService = Service("1", "Barba", 1, 30.0)
        
        val bookingKeep = Booking(
            id = bookingIdKeep,
            customer = testCustomer,
            dateTime = today.atTime(LocalTime.of(10, 0)),
            service = testService
        )
        
        val bookingRemove = Booking(
            id = bookingIdRemove,
            customer = testCustomer,
            dateTime = today.atTime(LocalTime.of(11, 0)),
            service = testService
        )
        
        // 1. Setup Week Rule: only 10:00 and 12:00 are available (11:00 removed)
        val testRule = WeekDayAvailableTimes(
            weekDay = weekdayName,
            open = true,
            timeSlots = mapOf(
                "10:00" to true,
                "11:00" to false, // This will cause bookingRemove to be deleted
                "12:00" to true
            )
        )
        DayModel.updateWeekDayAvailableTime(testRule)
        
        // 2. Setup existing Day with 10:00 and 11:00 slots booked
        val initialDay = Day(
            date = today,
            open = true,
            timeSlots = listOf(
                TimeSlot(LocalTime.of(10, 0), booked = true, bookingId = bookingIdKeep),
                TimeSlot(LocalTime.of(11, 0), booked = true, bookingId = bookingIdRemove)
            )
        )
        DayModel.updateDay(initialDay)
        
        // 3. Create Bookings in collection
        BookingModel.createBooking(bookingKeep)
        BookingModel.createBooking(bookingRemove)
        
        // 4. Run Update
        DayService.updateCurrentWeekSchedules()
        
        // 5. Assertions
        val updatedDay = DayModel.getBookingDay(today)
        assertNotNull("Updated day should exist", updatedDay)
        assertEquals("Should have 2 time slots after rule update", 2, updatedDay!!.timeSlots.size)
        
        val slot10 = updatedDay.timeSlots.find { it.hour == LocalTime.of(10, 0) }
        assertNotNull("10:00 slot should exist", slot10)
        assertTrue("10:00 slot should still be booked", slot10!!.booked)
        assertEquals(bookingIdKeep, slot10.bookingId)
        
        val slot11 = updatedDay.timeSlots.find { it.hour == LocalTime.of(11, 0) }
        assertNull("11:00 slot should be removed", slot11)
        
        val slot12 = updatedDay.timeSlots.find { it.hour == LocalTime.of(12, 0) }
        assertNotNull("12:00 slot should exist (new from rule)", slot12)
        assertFalse("12:00 slot should not be booked", slot12!!.booked)
        
        // 6. Verify Bookings collection
        val retrievedKeep = try { BookingModel.getBooking(bookingIdKeep) } catch (e: Exception) { null }
        assertNotNull("Booking 10:00 should still exist in database", retrievedKeep)
        
        val retrievedRemove = try { BookingModel.getBooking(bookingIdRemove) } catch (e: Exception) { null }
        assertNull("Booking 11:00 should be DELETED from database", retrievedRemove)
    }
}
