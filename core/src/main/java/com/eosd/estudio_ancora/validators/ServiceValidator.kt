package com.eosd.estudio_ancora.validators

import com.eosd.estudio_ancora.states.ServiceFormState

object ServiceValidator {
    fun validate(state: ServiceFormState): ServiceFormState {
        val nameError = if (state.name.isBlank()) "O nome é obrigatório" else null

        val priceDouble = state.price.toDoubleOrNull()
        val priceError = when {
            state.price.isBlank() -> "O preço é obrigatório"
            priceDouble == null -> "Preço inválido"
            priceDouble <= 0 -> "O preço deve ser maior que zero"
            else -> null
        }

        val durationInt = state.duration.toIntOrNull()
        val durationError = when {
            state.duration.isBlank() -> "A duração é obrigatória"
            durationInt == null -> "Duração inválida"
            durationInt <= 0 -> "A duração deve ser maior que zero"
            else -> null
        }

        val isFormValid = nameError == null && priceError == null && durationError == null

        return state.copy(
            nameError = nameError,
            priceError = priceError,
            durationError = durationError,
            isFormValid = isFormValid
        )
    }
}
