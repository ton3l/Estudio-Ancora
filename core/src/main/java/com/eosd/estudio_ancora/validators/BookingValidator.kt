package com.eosd.estudio_ancora.views.validators

import com.eosd.estudio_ancora.core.states.BookingFormState
import java.time.LocalDateTime

object BookingValidator {
    fun validate(bookingFormState: BookingFormState): BookingFormState {
        val dateTimeError = if (bookingFormState.dateTime.isBefore(
                LocalDateTime.now()
            )
        ) "A data e hora devem ser futuras" else null

        val customerNameError = validateCustomerName(bookingFormState.customer.name)

        val customerPhoneNumberError =
            validateCustomerPhoneNumber(bookingFormState.customer.phoneNumber)

        val serviceError = if (bookingFormState.service == null)
            "O serviço é obrigatório"
        else
            null

        val isFormValid = dateTimeError == null &&
                customerNameError == null &&
                customerPhoneNumberError == null &&
                serviceError == null

        return bookingFormState.copy(
            dateTimeError = dateTimeError,
            customerNameError = customerNameError,
            customerPhoneNumberError = customerPhoneNumberError,
            serviceError = serviceError,
            isFormValid = isFormValid
        )
    }

    private fun validateCustomerName(customerName: String): String? {
        return if (customerName.isBlank()) {
            "O nome do cliente é obrigatório"
        } else if (customerName.length < 3) {
            "O nome deve conter ao menos 3 caractéres"
        } else {
            null
        }
    }

    private fun validateCustomerPhoneNumber(phoneNumber: String): String? {
        return if (phoneNumber.isBlank()) {
            "O número de telefone do cliente é obrigatório"
        } else if (phoneNumber.length < 11) {
            "O número de telefone deve ser válido"
        } else {
            null
        }
    }
}