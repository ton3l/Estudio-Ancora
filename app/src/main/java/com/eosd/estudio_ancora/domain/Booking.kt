package com.eosd.estudio_ancora.domain

import java.time.LocalDateTime

data class Booking(
    val id: String,
    val customer: Customer,
    val dateTime: LocalDateTime,
    val service: Service
)
