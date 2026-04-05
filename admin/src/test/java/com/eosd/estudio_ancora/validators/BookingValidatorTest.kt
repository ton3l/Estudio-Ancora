package com.eosd.estudio_ancora.validators

import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.states.BookingFormState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class BookingValidatorTest {

    private val validCustomer = Customer("John Doe", "11987654321")
    private val validService = Service("s1", "Corte", 1, 30.0)
    private val futureDateTime = LocalDateTime.now().plusDays(1)

    @Test
    fun `valid form state should return isFormValid true`() {
        val state = BookingFormState(
            dateTime = futureDateTime,
            customer = validCustomer,
            service = validService
        )
        
        val validatedState = BookingValidator.validate(state)
        
        assertTrue(validatedState.isFormValid)
        assertNull(validatedState.dateTimeError)
        assertNull(validatedState.customerNameError)
        assertNull(validatedState.customerPhoneNumberError)
        assertNull(validatedState.serviceError)
    }

    @Test
    fun `past date should return error`() {
        val state = BookingFormState(
            dateTime = LocalDateTime.now().minusHours(1),
            customer = validCustomer,
            service = validService
        )
        
        val validatedState = BookingValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.dateTimeError)
    }

    @Test
    fun `short customer name should return error`() {
        val state = BookingFormState(
            dateTime = futureDateTime,
            customer = validCustomer.copy(name = "Jo"),
            service = validService
        )
        
        val validatedState = BookingValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.customerNameError)
    }

    @Test
    fun `blank customer name should return error`() {
        val state = BookingFormState(
            dateTime = futureDateTime,
            customer = validCustomer.copy(name = ""),
            service = validService
        )
        
        val validatedState = BookingValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.customerNameError)
    }

    @Test
    fun `short phone number should return error`() {
        val state = BookingFormState(
            dateTime = futureDateTime,
            customer = validCustomer.copy(phoneNumber = "1234567890"), // 10 digits
            service = validService
        )
        
        val validatedState = BookingValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.customerPhoneNumberError)
    }

    @Test
    fun `missing service should return error`() {
        val state = BookingFormState(
            dateTime = futureDateTime,
            customer = validCustomer,
            service = null
        )
        
        val validatedState = BookingValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.serviceError)
    }
}
