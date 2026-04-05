package com.eosd.estudio_ancora.states

data class ServiceFormState(
    val id: String? = null,
    val name: String = "",
    val price: String = "",
    val duration: String = "",
    val nameError: String? = null,
    val priceError: String? = null,
    val durationError: String? = null,
    val isFormValid: Boolean = false
)
