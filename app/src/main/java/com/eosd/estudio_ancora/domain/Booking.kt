package com.eosd.estudio_ancora.domain

import java.time.LocalDateTime

data class Booking(
    val id: String,
    val customer: String,
    val dateTime: LocalDateTime,
    val phoneNumber: String,
    val service: Service
)
