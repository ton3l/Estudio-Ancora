package com.eosd.estudio_ancora.admin.views.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes
import com.eosd.estudio_ancora.services.DayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminTimesViewModel : ViewModel() {

    private val _weekRules = MutableStateFlow<List<WeekDayAvailableTimes>>(emptyList())
    val weekRules: StateFlow<List<WeekDayAvailableTimes>> = _weekRules.asStateFlow()

    private val _selectedDayRule = MutableStateFlow<WeekDayAvailableTimes?>(null)
    val selectedDayRule: StateFlow<WeekDayAvailableTimes?> = _selectedDayRule.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val dayTranslations = mapOf(
        "sunday" to "Domingo",
        "monday" to "Segunda-feira",
        "tuesday" to "Terça-feira",
        "wednesday" to "Quarta-feira",
        "thursday" to "Quinta-feira",
        "friday" to "Sexta-feira",
        "saturday" to "Sábado"
    )

    private val dayOrder = listOf(
        "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"
    )

    init {
        fetchWeekRules()
    }

    fun fetchWeekRules() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val rules = DayService.getWeekRules()
                _weekRules.value = rules.sortedBy { dayOrder.indexOf(it.weekDay) }
            } catch (e: Exception) {
                // Ignore for now
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectDay(dayId: String) {
        val rule = _weekRules.value.find { it.weekDay == dayId }
        _selectedDayRule.value = rule?.copy() // create a copy for editing
    }

    fun toggleDayOpen(isOpen: Boolean) {
        _selectedDayRule.update { it?.copy(open = isOpen) }
    }

    fun toggleTimeSlot(timeStr: String, isChecked: Boolean) {
        _selectedDayRule.update { rule ->
            if (rule == null) return@update null
            val updatedSlots = rule.timeSlots.toMutableMap()
            updatedSlots[timeStr] = isChecked
            rule.copy(timeSlots = updatedSlots)
        }
    }

    fun saveSelectedDay(onSuccess: () -> Unit) {
        val ruleToSave = _selectedDayRule.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                DayService.updateWeekRule(ruleToSave)
                fetchWeekRules()
                onSuccess()
            } catch (e: Exception) {
                // Ignore
            } finally {
                _isLoading.value = false
            }
        }
    }
}
