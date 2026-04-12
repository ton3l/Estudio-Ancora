package com.eosd.estudio_ancora.domain

import androidx.annotation.Keep

@Keep
data class Customer(
    val name: String,
    val phoneNumber: String,
)
