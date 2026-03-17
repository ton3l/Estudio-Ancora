package com.eosd.estudio_ancora.models.booking

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.booking.dtos.BookingDocument
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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

    @Test
    fun createBooking_successfullyPersistsToFirestore() = runBlocking {
        // 1. Arrange: Preparar os dados para o agendamento
        val testService = Service(
            id = "test-service-id",
            name = "Corte de Cabelo Teste",
            duration = 30,
            price = 40.0
        )

        val testBookingId = UUID.randomUUID().toString()
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

        // 4. Cleanup: Opcional, remover o documento de teste para manter o banco limpo
        // docRef.delete().await()
    }
}
