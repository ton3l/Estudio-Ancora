package com.eosd.estudio_ancora.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.models.day.DayModel
import com.eosd.estudio_ancora.views.interfaces.BookingInfo
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Teste instrumentado para o BookingService.
 * Este teste valida o fluxo completo de criação de um agendamento.
 */
@RunWith(AndroidJUnit4::class)
class BookingServiceTest {

    @Test
    fun addBooking_shouldUpdateDayStateAndCreateBookingRecord() = runBlocking {
        // 1. Arrange: Preparar os dados de teste
        val testDate = LocalDateTime.of(1000, 10, 10, 20, 0) // Uma data futura
        val testCustomer = Customer(name = "Cliente Teste", phoneNumber = "11999999999")
        val testService = Service(
            id = "test-service-id",
            name = "Corte de Teste",
            duration = 30,
            price = 50.0
        )

        val bookingInfo = object : BookingInfo {
            override val dateTime: LocalDateTime = testDate
            override val customer: Customer = testCustomer
            override val service: Service = testService
        }

        // 2. Act: Executar o serviço de agendamento
        // Nota: Este passo provavelmente falhará devido aos bugs identificados no DayModel e TimeSlotDocument
        BookingService.addBooking(bookingInfo)
        try {
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

        // Nota: Para validar o BookingModel.createBooking, idealmente teríamos um método getBooking(id)
        // Como o ID é gerado internamente pelo service, usamos o bookingId recuperado do slot do dia.
        // Se chegamos até aqui e o bookingId existe, o fluxo de criação foi disparado.
    }
}
