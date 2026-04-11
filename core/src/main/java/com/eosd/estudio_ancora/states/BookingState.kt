package com.eosd.estudio_ancora.states

sealed interface BookingState {
    data object Idle : BookingState
    data object Loading : BookingState
    data object Success : BookingState
    data class Error(val message: String) : BookingState
}
