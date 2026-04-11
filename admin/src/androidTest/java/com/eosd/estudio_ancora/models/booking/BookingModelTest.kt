package com.eosd.estudio_ancora.models.booking

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.booking.dtos.BookingDocument
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.util.UUID

/**
 * Teste instrumentado para o BookingModel.
 * Este teste interage com a instância configurada do Firestore para garantir
 * que a persistência de agendamentos está funcionando corretamente.
 */
@RunWith(AndroidJUnit4::class)
class BookingModelTest {

    private val createdBookingIds = mutableListOf<String>()

    @Before
    fun setUp() {
        createdBookingIds.clear()
    }

    @After
    fun tearDown() {
        runBlocking {
            createdBookingIds.forEach { id ->
                firestore.collection("bookings").document(id).delete().await()
            }
            createdBookingIds.clear()
        }
    }

    @Test
    fun createBooking_successfullyPersistsToFirestore() {
        runBlocking {
            // 1. Arrange: Preparar os dados para o agendamento
            val testService = Service(
                id = "test-service-id",
                name = "Corte de Cabelo Teste",
                duration = 30,
                price = 40.0
            )

            val testBookingId = UUID.randomUUID().toString()
            createdBookingIds.add(testBookingId)
            val testBooking = Booking(
                id = testBookingId,
                customer = Customer(name = "John Doe", phoneNumber = "1234567890"),
                dateTime = LocalDateTime.now().withNano(0), // Firestore ignora nanos, então limpamos para o assert
                service = testService
            )

            // 2. Act: Chamar o método createBooking
            BookingModel.createBooking(testBooking)

            // 3. Assert & Verify Content: Buscar o documento diretamente do Firestore para validar os dados
            val docRef = firestore.collection("bookings").document(testBookingId)
            val snapshot = docRef.get().await()
            
            assertTrue("O documento deveria existir no Firestore", snapshot.exists())
            
            val savedBooking = snapshot.toObject(BookingDocument::class.java)
            assertNotNull("O documento salvo não deveria ser nulo após a conversão", savedBooking)
            
            assertEquals(
                "O ID do agendamento deve coincidir",
                testBookingId,
                savedBooking?.id
            )
            assertEquals(
                "O nome do cliente deve coincidir",
                testBooking.customer.name,
                savedBooking?.customer?.name
            )
            assertEquals(
                "O nome do serviço deve coincidir",
                testService.name,
                savedBooking?.service?.name
            )
        }
    }

    @Test
    fun getBooking_returnsCorrectBooking() {
        runBlocking {
            // Arrange
            val testBookingId = UUID.randomUUID().toString()
            createdBookingIds.add(testBookingId)
            val testBooking = createTestBooking(testBookingId)
            BookingModel.createBooking(testBooking)

            // Act
            val result = BookingModel.getBooking(testBookingId)

            // Assert
            assertEquals(testBookingId, result.id)
            assertEquals(testBooking.customer.name, result.customer.name)
        }
    }

    @Test
    fun getBookings_returnsListOfBookings() {
        runBlocking {
            // Arrange
            val id1 = UUID.randomUUID().toString()
            val id2 = UUID.randomUUID().toString()
            createdBookingIds.add(id1)
            createdBookingIds.add(id2)
            val b1 = createTestBooking(id1)
            val b2 = createTestBooking(id2)
            BookingModel.createBooking(b1)
            BookingModel.createBooking(b2)

            // Act
            val results = BookingModel.getBookings(listOf(id1, id2))

            // Assert
            assertEquals(2, results.size)
            assertTrue(results.any { it.id == id1 })
            assertTrue(results.any { it.id == id2 })
        }
    }

    @Test
    fun deleteBooking_removesFromFirestore() {
        runBlocking {
            // Arrange
            val testBookingId = UUID.randomUUID().toString()
            createdBookingIds.add(testBookingId)
            val testBooking = createTestBooking(testBookingId)
            BookingModel.createBooking(testBooking)

            // Act
            BookingModel.deleteBooking(testBooking)

            // Assert
            val docRef = firestore.collection("bookings").document(testBookingId)
            val snapshot = docRef.get().await()
            assertTrue("O documento não deveria mais existir no Firestore", !snapshot.exists())
        }
    }

    @Test
    fun getAllFutureBookings_returnsSortedFutureBookings() {
        runBlocking {
            // Arrange
            val pastId = UUID.randomUUID().toString()
            val futureId1 = UUID.randomUUID().toString()
            val futureId2 = UUID.randomUUID().toString()
            createdBookingIds.addAll(listOf(pastId, futureId1, futureId2))
            
            val pastBooking = createTestBooking(pastId).copy(dateTime = LocalDateTime.now().minusDays(1))
            val futureBooking1 = createTestBooking(futureId1).copy(dateTime = LocalDateTime.now().plusDays(2))
            val futureBooking2 = createTestBooking(futureId2).copy(dateTime = LocalDateTime.now().plusDays(1))

            BookingModel.createBooking(pastBooking)
            BookingModel.createBooking(futureBooking1)
            BookingModel.createBooking(futureBooking2)

            // Act
            val results = BookingModel.getAllFutureBookings()

            // Assert
            // It should not contain pastBooking
            assertTrue(!results.any { it.id == pastId })
            
            // It should contain future bookings
            assertTrue(results.any { it.id == futureId1 })
            assertTrue(results.any { it.id == futureId2 })

            // It should be sorted ascending (futureBooking2 before futureBooking1)
            val index1 = results.indexOfFirst { it.id == futureId2 }
            val index2 = results.indexOfFirst { it.id == futureId1 }
            assertTrue("Future booking 2 (plus 1 day) should be before Future booking 1 (plus 2 days)", index1 < index2)
        }
    }

    private fun createTestBooking(id: String): Booking {
        return Booking(
            id = id,
            customer = Customer(name = "John Doe", phoneNumber = "1234567890"),
            dateTime = LocalDateTime.now().withNano(0),
            service = Service(id = "service-id", name = "Service", duration = 30, price = 50.0)
        )
    }
}
