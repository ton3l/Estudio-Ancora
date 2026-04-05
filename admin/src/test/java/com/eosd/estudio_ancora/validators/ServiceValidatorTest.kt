package com.eosd.estudio_ancora.validators

import com.eosd.estudio_ancora.states.ServiceFormState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ServiceValidatorTest {

    @Test
    fun `valid service form state should return isFormValid true`() {
        val state = ServiceFormState(
            name = "Corte de Cabelo",
            price = "50.0",
            duration = "1"
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertTrue(validatedState.isFormValid)
        assertNull(validatedState.nameError)
        assertNull(validatedState.priceError)
        assertNull(validatedState.durationError)
    }

    @Test
    fun `blank name should return error`() {
        val state = ServiceFormState(
            name = "",
            price = "50.0",
            duration = "1"
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.nameError)
    }

    @Test
    fun `invalid price should return error`() {
        val state = ServiceFormState(
            name = "Corte",
            price = "abc",
            duration = "1"
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.priceError)
    }

    @Test
    fun `zero price should return error`() {
        val state = ServiceFormState(
            name = "Corte",
            price = "0",
            duration = "1"
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.priceError)
    }

    @Test
    fun `blank price should return error`() {
        val state = ServiceFormState(
            name = "Corte",
            price = "",
            duration = "1"
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.priceError)
    }

    @Test
    fun `invalid duration should return error`() {
        val state = ServiceFormState(
            name = "Corte",
            price = "50",
            duration = "xyz"
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.durationError)
    }

    @Test
    fun `zero duration should return error`() {
        val state = ServiceFormState(
            name = "Corte",
            price = "50",
            duration = "0"
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.durationError)
    }

    @Test
    fun `blank duration should return error`() {
        val state = ServiceFormState(
            name = "Corte",
            price = "50",
            duration = ""
        )
        
        val validatedState = ServiceValidator.validate(state)
        
        assertFalse(validatedState.isFormValid)
        assertNotNull(validatedState.durationError)
    }
}
