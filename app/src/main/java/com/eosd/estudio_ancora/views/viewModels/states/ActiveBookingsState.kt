package com.eosd.estudio_ancora.views.viewModels.states

import com.eosd.estudio_ancora.domain.Booking

sealed interface ActiveBookingsState {
    object Loading : ActiveBookingsState
    data class Error(val message: String) : ActiveBookingsState
    data class Success(val bookings: List<Booking>) : ActiveBookingsState
}