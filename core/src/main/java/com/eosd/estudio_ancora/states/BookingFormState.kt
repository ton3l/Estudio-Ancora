package com.eosd.estudio_ancora.core.states

import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import java.time.LocalDateTime

data class BookingFormState(
    val dateTime: LocalDateTime = LocalDateTime.parse("0001-01-01T01:01:01"),
    val customer: Customer = Customer(
        name = "",
        phoneNumber = ""
    ),
    val service: Service? = null,

    val dateTimeError: String? = null,
    val customerNameError: String? = null,
    val customerPhoneNumberError: String? = null,
    val serviceError: String? = null,

    val isFormValid: Boolean = false,

    val isBooking: Boolean = false
)
