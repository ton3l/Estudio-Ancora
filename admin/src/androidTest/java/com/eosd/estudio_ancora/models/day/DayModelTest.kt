package com.eosd.estudio_ancora.models.day

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.domain.Day
import com.eosd.estudio_ancora.domain.TimeSlot
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.day.dtos.DayDocument
import com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class DayModelTest {
    private val bookingDaysCollection = firestore.collection("booking-days")
    private val weekAvailableTimesCollection = firestore.collection("week-available-times")
    
    private val testDate = LocalDate.now().plusDays(100) // Future date to avoid conflicts
    private val testDayOfWeek = testDate.dayOfWeek.toString().lowercase()

    @Before
    fun setUp() = runBlocking {
        cleanUp()
    }

    @After
    fun tearDown() = runBlocking {
        cleanUp()
    }

    private suspend fun cleanUp() {
        bookingDaysCollection.document(testDate.toString()).delete().await()
    }

    @Test
    fun getDay_fromBookingDays_whenExists() = runBlocking {
        // Arrange
        val day = Day(
            date = testDate,
            open = true,
            timeSlots = listOf(
                TimeSlot(hour = LocalTime.of(9, 0), booked = false),
                TimeSlot(hour = LocalTime.of(10, 0), booked = true, bookingId = "some-id")
            )
        )
        bookingDaysCollection.document(testDate.toString()).set(DayDocument.toDocument(day)).await()

        // Act
        val result = DayModel.getDay(testDate)

        // Assert
        assertEquals(testDate, result.date)
        assertTrue(result.open)
        assertEquals(2, result.timeSlots.size)
        assertTrue(result.timeSlots.any { it.hour == LocalTime.of(9, 0) && !it.booked })
        assertTrue(result.timeSlots.any { it.hour == LocalTime.of(10, 0) && it.booked && it.bookingId == "some-id" })
    }

    @Test
    fun getDay_fromWeekAvailable_whenBookingDayDoesNotExist() = runBlocking {
        // Arrange
        val weekDay = WeekDayAvailableTimes(
            weekDay = testDayOfWeek,
            open = true,
            timeSlots = mapOf("09:00" to true, "10:00" to true, "11:00" to false)
        )
        weekAvailableTimesCollection.document(testDayOfWeek).set(weekDay).await()

        // Act
        val result = DayModel.getDay(testDate)

        // Assert
        assertEquals(testDate, result.date)
        assertTrue(result.open)
        assertEquals(2, result.timeSlots.size) // Only 09:00 and 10:00 should be there as 11:00 is false
        assertTrue(result.timeSlots.any { it.hour == LocalTime.of(9, 0) })
        assertTrue(result.timeSlots.any { it.hour == LocalTime.of(10, 0) })
    }

    @Test
    fun getBookingDay_returnsNull_whenDoesNotExist() = runBlocking {
        // Act
        val result = DayModel.getBookingDay(testDate)

        // Assert
        assertNull(result)
    }

    @Test
    fun updateDay_persistsToFirestore() = runBlocking {
        // Arrange
        val day = Day(
            date = testDate,
            open = false,
            timeSlots = emptyList()
        )

        // Act
        DayModel.updateDay(day)

        // Assert
        val snapshot = bookingDaysCollection.document(testDate.toString()).get().await()
        assertTrue(snapshot.exists())
        assertEquals(false, snapshot.getBoolean("open"))
    }
}
