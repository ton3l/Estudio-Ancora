package com.eosd.estudio_ancora.views.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eosd.estudio_ancora.services.DayService
import com.eosd.estudio_ancora.views.viewModels.states.AvailableTimesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
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


    fun onDaySelected(date: LocalDate) {
        _selectedDay.value = if (_selectedDay.value == date) null else date
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
}
