package com.eosd.estudio_ancora.models.booking

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookingLocalPersistenceTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() = runBlocking {
        // Clear all booking IDs before each test to ensure a clean state
        val currentIds = BookingLocalPersistence.getBookingIds(context).first()
        for (id in currentIds) {
            BookingLocalPersistence.removeBookingId(context, id)
        }
    }

    @Test
    fun addBookingId_persistsIdLocally() = runBlocking {
        // Arrange
        val testId = "test-booking-id-123"

        // Act
        BookingLocalPersistence.addBookingId(context, testId)

        // Assert
        val currentIds = BookingLocalPersistence.getBookingIds(context).first()
        assertTrue("O ID do agendamento deveria estar persistido localmente", currentIds.contains(testId))
    }

    @Test
    fun removeBookingId_removesIdFromLocalPersistence() = runBlocking {
        // Arrange
        val testId = "test-booking-id-456"
        BookingLocalPersistence.addBookingId(context, testId)

        // Act
        BookingLocalPersistence.removeBookingId(context, testId)

        // Assert
        val currentIds = BookingLocalPersistence.getBookingIds(context).first()
        assertFalse("O ID do agendamento não deveria mais estar persistido localmente", currentIds.contains(testId))
    }

    @Test
    fun getBookingIds_returnsAllPersistedIds() = runBlocking {
        // Arrange
        val testId1 = "id-1"
        val testId2 = "id-2"
        BookingLocalPersistence.addBookingId(context, testId1)
        BookingLocalPersistence.addBookingId(context, testId2)

        // Act
        val currentIds = BookingLocalPersistence.getBookingIds(context).first()

        // Assert
        assertEquals(2, currentIds.size)
        assertTrue(currentIds.contains(testId1))
        assertTrue(currentIds.contains(testId2))
    }
}
