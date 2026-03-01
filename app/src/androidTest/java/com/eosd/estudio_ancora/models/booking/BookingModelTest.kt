package com.eosd.estudio_ancora.models.booking

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.domain.Service
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

/**
 * Teste instrumentado para o BookingModel.
 * Como o BookingModel utiliza o Firebase Firestore diretamente, este teste
 * interage com a instância configurada do Firestore.
 */
@RunWith(AndroidJUnit4::class)
class BookingModelTest {

    @Test
    fun addBooking_successfullyCreatesDocument() = runBlocking {
        // 1. Arrange: Preparar os dados para o agendamento
        val testService = Service(
            id = "test-service-id",
            name = "Corte de Cabelo Teste",
            duration = 30,
            price = 40.0
        )
        
        val testBooking = Booking(
            id = "", // O ID será gerado pelo Firestore
            customer = "Usuário de Teste",
            dateTime = LocalDateTime.now().withNano(0), // Removendo nanos para evitar problemas de precisão no comparativo
            phoneNumber = "11988887777",
            service = testService
        )

        // 2. Act: Chamar o método addBooking
        val documentRef = BookingModel.addBooking(testBooking)

        // 3. Assert: Verificar se o documento foi criado com sucesso
        assertNotNull("A referência do documento não deveria ser nula", documentRef)
        assertNotNull("O ID do documento gerado não deveria ser nulo", documentRef.id)

        // 4. Verify Content: Buscar o documento diretamente do Firestore para validar os dados
        val snapshot = documentRef.get().await()
        val savedBooking = snapshot.toObject(BookingDocument::class.java)

        assertNotNull("O documento salvo deveria existir no Firestore", savedBooking)
        assertEquals("O nome do cliente deve coincidir", testBooking.customer, savedBooking?.customer)
        assertEquals("O número de telefone deve coincidir", testBooking.phoneNumber, savedBooking?.phoneNumber)
        assertEquals("O nome do serviço deve coincidir", testService.name, savedBooking?.service?.name)

        // 5. Cleanup: Remover o documento de teste para manter o banco limpo
//        documentRef.delete().await()
    }
}
