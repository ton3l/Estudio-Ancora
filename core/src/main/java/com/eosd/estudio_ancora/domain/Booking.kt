package com.eosd.estudio_ancora.domain

import androidx.annotation.Keep
import java.time.LocalDateTime

@Keep
data class Booking(
    val id: String,
    val customer: Customer,
    val dateTime: LocalDateTime,
    val service: Service
)
