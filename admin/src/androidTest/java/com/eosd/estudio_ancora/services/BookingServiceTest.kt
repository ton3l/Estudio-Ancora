package com.eosd.estudio_ancora.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.states.BookingFormState
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

/**
 * Teste instrumentado para o BookingService.
 * Este teste valida o fluxo completo de criação de um agendamento.
 */
@RunWith(AndroidJUnit4::class)
class BookingServiceTest {

    @Before
    fun setUp() {
        runBlocking {
            val testDate = LocalDateTime.now().plusDays(10).toLocalDate()
            firestore.collection("booking-days").document(testDate.toString()).delete().await()
        }
    }

    @Test
    fun addBooking_shouldUpdateDayStateAndCreateBookingRecord() = runBlocking {
        // 1. Arrange: Preparar os dados de teste
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val testDate = LocalDateTime.now().minusYears(1000).withHour(20).withMinute(0).withSecond(0).withNano(0)
        val testCustomer = Customer(name = "Cliente Teste", phoneNumber = "11999999999")
        val testService = Service(
            id = "test-service-id",
            name = "Corte de Teste",
            duration = 1,
            price = 50.0
        )

        val bookingInfo = BookingFormState(
            dateTime = testDate,
            customer = testCustomer,
            service = testService
        )

        // 2. Act: Executar o serviço de agendamento
        try {
            BookingService.addBooking(context = context, bookingInfo = bookingInfo)
        } catch (e: Exception) {
            fail("O addBooking falhou com uma exceção: ${e.message}")
        }

        // 3. Assert: Verificar se o estado no banco de dados está correto
        // Validar se o horário no dia foi marcado como reservado (booked = true)
        val day = DayModel.getBookingDay(testDate.toLocalDate())
        assertNotNull("O documento do dia deveria ter sido criado/encontrado", day)

        val timeSlot = day?.timeSlots?.find { it.hour == testDate.toLocalTime() }
        assertNotNull("O slot de horário correspondente deveria existir", timeSlot)
        assertTrue("O horário deveria estar marcado como reservado (booked)", timeSlot!!.booked)
        assertNotNull("O bookingId no slot não deveria estar nulo", timeSlot.bookingId)
    }
}
