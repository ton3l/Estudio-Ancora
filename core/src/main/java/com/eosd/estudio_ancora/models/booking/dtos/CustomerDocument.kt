package com.eosd.estudio_ancora.models.booking.dtos

import androidx.annotation.Keep
import com.eosd.estudio_ancora.domain.Customer

@Keep
data class CustomerDocument(
    val name: String = "undefined",
    val phoneNumber: String = "undefined",
) {
    fun toEntity(): Customer {
        return Customer(
            name = name,
            phoneNumber = phoneNumber
        )
    }

    companion object {
        fun toDocument(customer: Customer): CustomerDocument {
            return CustomerDocument(
                name = customer.name,
                phoneNumber = customer.phoneNumber
            )
        }
    }
}

