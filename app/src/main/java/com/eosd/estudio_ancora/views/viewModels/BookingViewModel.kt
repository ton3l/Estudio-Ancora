package com.eosd.estudio_ancora.views.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.services.DayService
import com.eosd.estudio_ancora.services.ServiceService
import com.eosd.estudio_ancora.views.interfaces.BookingInfo
import com.eosd.estudio_ancora.views.viewModels.states.AvailableTimesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class BookingViewModel : ViewModel() {
    private val _selectedDay = MutableStateFlow<LocalDate?>(null)
    val selectedDay: StateFlow<LocalDate?> = _selectedDay.asStateFlow()

    private val _currentDayAvailableTimes = MutableStateFlow<AvailableTimesState>(
        AvailableTimesState.Loading
    )
    val currentDayAvailableTimes: StateFlow<AvailableTimesState> =
        _currentDayAvailableTimes.asStateFlow()

    private val _selectedTime = MutableStateFlow<LocalTime?>(null)
    val selectedTime: StateFlow<LocalTime?> = _selectedTime

    private val _customerName = MutableStateFlow<String>("")
    val customerName: StateFlow<String> = _customerName.asStateFlow()

    private val _customerPhoneNumber = MutableStateFlow<String>("")
    val customerPhoneNumber: StateFlow<String> = _customerPhoneNumber.asStateFlow()

    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService: StateFlow<Service?> = _selectedService.asStateFlow()

    private val _serviceList = MutableStateFlow<List<Service>>(emptyList())
    val serviceList: StateFlow<List<Service>> = _serviceList.asStateFlow()

    init {
        fetchServices()
    }

    val bookingInfo: StateFlow<BookingInfo> = combine(
        _selectedDay,
        _selectedTime,
        _customerName,
        _customerPhoneNumber,
        _selectedService
    ) { day, time, customerName, customerPhoneNumber, service ->
        if (service == null) {
            object : BookingInfo {
                override val dateTime = day!!.atTime(time)
                override val customer = Customer(
                    name = customerName,
                    phoneNumber = customerPhoneNumber
                )
                override val service = Service(
                    id = "0",
                    name = "",
                    duration = 0,
                    price = 0.0
                )
            }
        } else {
            object : BookingInfo {
                override val dateTime = day!!.atTime(time)
                override val customer = Customer(
                    name = customerName,
                    phoneNumber = customerPhoneNumber
                )
                override val service = service!!
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = object : BookingInfo {
            override val dateTime = LocalDateTime.now()
            override val customer = Customer(
                name = "",
                phoneNumber = ""
            )
            override val service = Service(
                id = "0",
                name = "",
                duration = 0,
                price = 0.0
            )
        }
    )

    fun onDaySelected(date: LocalDate) {
        _selectedDay.value = date
        _currentDayAvailableTimes.value = AvailableTimesState.Loading

        if (_selectedDay.value != null) {
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
    }

    fun onTimeSelected(time: LocalTime) {
        _selectedTime.value = time
    }

    fun onCustomerNameChanged(name: String): Unit {
        _customerName.value = name
    }

    fun onCustomerPhoneNumberChanged(phoneNumber: String) {
        val numericRegex = Regex("[^0-9]")
        val stripped = numericRegex.replace(phoneNumber, "")

        val customerPhoneNumber = if (stripped.length >= 11) {
            stripped.substring(0..10)
        } else {
            stripped
        }

        _customerPhoneNumber.value = customerPhoneNumber
    }

    fun onServiceSelected(service: Service) {
        _selectedService.value = service
    }

    private fun fetchServices() {
        viewModelScope.launch {
            try {
                _serviceList.value = ServiceService.getAllServices()
            } catch (_: Exception) {
                TODO("Handle error")
            }
        }
    }
}
