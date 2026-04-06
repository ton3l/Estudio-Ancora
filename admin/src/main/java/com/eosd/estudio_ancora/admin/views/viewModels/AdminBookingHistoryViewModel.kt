package com.eosd.estudio_ancora.admin.views.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eosd.estudio_ancora.domain.Booking
import com.eosd.estudio_ancora.services.BookingService
import com.eosd.estudio_ancora.states.BookingFilterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class AdminBookingHistoryViewModel : ViewModel() {

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _filterFormState = MutableStateFlow(BookingFilterState())
    val filterFormState: StateFlow<BookingFilterState> = _filterFormState.asStateFlow()

    private val _appliedFilter = MutableStateFlow(BookingFilterState())
    val appliedFilter: StateFlow<BookingFilterState> = _appliedFilter.asStateFlow()

    val filteredBookings: StateFlow<List<Booking>> = combine(
        _bookings,
        _appliedFilter
    ) { bookingsList, filter ->
        bookingsList.filter { booking ->
            val nameMatch = filter.customerName.isBlank() ||
                    filter.customerName.lowercase() in booking.customer.name.lowercase()

            val serviceMatch = filter.serviceName.isBlank() ||
                    filter.serviceName.lowercase() in booking.service.name.lowercase()

            val filterDate = filter.date
            val dateMatch = filterDate == null || booking.dateTime.toLocalDate() == filterDate

            val bookingTimeStr = String.format("%02d:%02d", booking.dateTime.hour, booking.dateTime.minute)
            val timeMatch = filter.time.isBlank() || filter.time in bookingTimeStr

            nameMatch && serviceMatch && dateMatch && timeMatch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        fetchFutureBookings()
    }

    fun fetchFutureBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _bookings.value = BookingService.getAllFutureBookings()
            } catch (e: Exception) {
                // Ignore for now
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onCustomerNameFilterChanged(name: String) {
        _filterFormState.update { it.copy(customerName = name) }
    }

    fun onServiceFilterChanged(serviceName: String) {
        _filterFormState.update { it.copy(serviceName = serviceName) }
    }

    fun onDateFilterChanged(date: LocalDate?) {
        _filterFormState.update { it.copy(date = date) }
    }

    fun onTimeFilterChanged(time: String) {
        _filterFormState.update { it.copy(time = time) }
    }

    fun applyFilters() {
        _appliedFilter.value = _filterFormState.value
    }

    fun clearFilters() {
        val emptyFilter = BookingFilterState()
        _filterFormState.value = emptyFilter
        _appliedFilter.value = emptyFilter
    }

    fun deleteBooking(booking: Booking) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                BookingService.deleteBookingByAdmin(booking)
                fetchFutureBookings()
            } catch (e: Exception) {
                // Ignore
            } finally {
                _isLoading.value = false
            }
        }
    }
}
