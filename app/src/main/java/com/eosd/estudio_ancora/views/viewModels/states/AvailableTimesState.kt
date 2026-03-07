package com.eosd.estudio_ancora.views.viewModels.states

import java.time.LocalTime

sealed interface AvailableTimesState {
    object Loading : AvailableTimesState
    data class Error(val message: String) : AvailableTimesState
    data class Success(val availableTimes: List<LocalTime>) : AvailableTimesState
}