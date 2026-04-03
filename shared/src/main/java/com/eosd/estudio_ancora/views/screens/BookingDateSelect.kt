package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.states.AvailableTimesState
import com.eosd.estudio_ancora.views.components.Calendar
import com.eosd.estudio_ancora.views.components.TimeSelectBottomSheet
import com.eosd.estudio_ancora.views.viewModels.BookingViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDateSelect(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: BookingViewModel = viewModel(),
    onDateTimeSelected: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    availableTimesHandler: @Composable (AvailableTimesState, (LocalTime) -> Unit) -> Unit
) {
    val bookingFormState by viewModel.bookingFormState.collectAsStateWithLifecycle()
    val currentDayAvailableTimes by viewModel.currentDayAvailableTimes.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escolha a Data") },
                modifier = Modifier
                    .padding(
                        top = (paddingValues.calculateTopPadding() - 24.dp)
                        .coerceAtLeast(0.dp)
                    )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Calendar(selectedDay = bookingFormState.dateTime.toLocalDate()) {
                viewModel.onDaySelected(it.date)
                showBottomSheet = true
            }

            if (showBottomSheet)
                TimeSelectBottomSheet(
                    sheetState = sheetState,
                    availableTimesState = currentDayAvailableTimes,
                    onDismiss = { showBottomSheet = false },
                    onTimeSelected = { time ->
                        viewModel.onTimeSelected(time)
                        showBottomSheet = false
                        onDateTimeSelected()
                    },
                    availableTimesHandler = availableTimesHandler
                )
        }
    }
}