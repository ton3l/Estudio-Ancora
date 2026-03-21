package com.eosd.estudio_ancora.views.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.services.BookingService
import com.eosd.estudio_ancora.views.viewModels.states.ActiveBookingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val _bookings = MutableStateFlow<ActiveBookingsState>(ActiveBookingsState.Loading)
    val bookings: StateFlow<ActiveBookingsState> = _bookings.asStateFlow()

    init {
        fetchBookings()
    }

    fun fetchBookings() {
        _bookings.value = ActiveBookingsState.Loading
        viewModelScope.launch {
            _bookings.value = BookingService.getUserActiveBookings(getApplication())
        }
    }

    fun deleteBooking(booking: Booking) {
        viewModelScope.launch {
            BookingService.deleteBooking(getApplication(), booking)
            fetchBookings()
        }
    }
}
