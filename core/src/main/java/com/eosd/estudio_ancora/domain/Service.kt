package com.eosd.estudio_ancora.domain

import androidx.annotation.Keep

@Keep
data class Service(
    val id: String,
    val name: String,
    val duration: Int,
    val price: Double
)
