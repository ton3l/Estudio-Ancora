package com.eosd.estudio_ancora.views.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.services.BookingService
import com.eosd.estudio_ancora.services.DayService
import com.eosd.estudio_ancora.services.ServiceService
import com.eosd.estudio_ancora.validators.BookingValidator
import com.eosd.estudio_ancora.states.AvailableTimesState
import com.eosd.estudio_ancora.states.BookingFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class BookingViewModel(application: Application) : AndroidViewModel(application) {
    private val _currentDayAvailableTimes = MutableStateFlow<AvailableTimesState>(
        AvailableTimesState.Loading
    )
    val currentDayAvailableTimes: StateFlow<AvailableTimesState> =
        _currentDayAvailableTimes.asStateFlow()

    private val _serviceList = MutableStateFlow<List<Service>>(emptyList())
    val serviceList: StateFlow<List<Service>> = _serviceList.asStateFlow()

    private val _isBooking = MutableStateFlow<Boolean>(false)
    val isBooking: StateFlow<Boolean> = _isBooking.asStateFlow()

    private val _bookingFormState = MutableStateFlow<BookingFormState>(BookingFormState())
    val bookingFormState = _bookingFormState.asStateFlow()

    init {
        fetchServices()
    }

    fun onDaySelected(date: LocalDate) {
        _bookingFormState.update { currentBookingFormState ->
            currentBookingFormState.copy(
                dateTime = currentBookingFormState.dateTime.with(date)
            )
        }
        _currentDayAvailableTimes.value = AvailableTimesState.Loading

        viewModelScope.launch {
            try {
                _currentDayAvailableTimes.value = DayService.getDayAvailableTimes(date)
            } catch (_: Exception) {
                _currentDayAvailableTimes.value = AvailableTimesState.Error(
                    message = "Ocorreu um erro ao buscar os horários disponíveis"
                )
            }
        }
    }

    fun onTimeSelected(time: LocalTime) {
        _bookingFormState.update { currentBookingFormState ->
            currentBookingFormState.copy(
                dateTime = currentBookingFormState.dateTime.with(time)
            )
        }
    }

    fun onCustomerNameChanged(name: String): Unit {
        val customerName = name.filter { it.isLetter() || it.isWhitespace() }
        _bookingFormState.update { currentBookingFormState ->
            currentBookingFormState.copy(
                customer = currentBookingFormState.customer.copy(name = customerName)
            )
        }
    }

    fun onCustomerPhoneNumberChanged(phoneNumber: String) {
        val numericRegex = Regex("[^0-9]")
        val stripped = numericRegex.replace(phoneNumber, "")

        val customerPhoneNumber = if (stripped.length >= 11) {
            stripped.substring(0..10)
        } else {
            stripped
        }

        _bookingFormState.update { currentBookingFormState ->
            currentBookingFormState.copy(
                customer = currentBookingFormState.customer.copy(phoneNumber = customerPhoneNumber)
            )
        }
    }

    fun onServiceSelected(service: Service) {
        _bookingFormState.update { currentBookingFormState ->
            currentBookingFormState.copy(
                service = service
            )
        }
    }

    private fun fetchServices() {
        viewModelScope.launch {
            try {
                _serviceList.value = ServiceService.getAllServices()
            } catch (e: Exception) {
                throw e
                TODO("Handle error")
            }
        }
    }

    fun createBooking(onFinished: () -> Unit) {
        _bookingFormState.update { currentBookingFormState ->
            BookingValidator.validate(currentBookingFormState)
        }
        if (!_bookingFormState.value.isFormValid) return

        _isBooking.value = true

        viewModelScope.launch {
            try {
                BookingService.addBooking(
                    context = getApplication(),
                    bookingInfo = bookingFormState.value
                )
                _isBooking.value = false
                onFinished()
            } catch (e: Exception) {
                throw e // TODO handle error
            }
        }
    }
}
