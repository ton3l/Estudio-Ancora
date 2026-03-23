package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.views.components.Calendar
import com.eosd.estudio_ancora.views.utils.toHHmm
import com.eosd.estudio_ancora.views.viewModels.BookingViewModel
import com.eosd.estudio_ancora.views.viewModels.states.AvailableTimesState
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDateSelect(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: BookingViewModel = viewModel(),
    onDateTimeSelected: () -> Unit = {},
    onBackPressed: () -> Unit = {},
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
                    .padding(top = (paddingValues.calculateTopPadding() - 24.dp).coerceAtLeast(0.dp))
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Calendar(selectedDay = bookingFormState.dateTime.toLocalDate()) {
                viewModel.onDaySelected(it.date)
                showBottomSheet = true
            }

            if (showBottomSheet)
                TimeSelect(
                    sheetState = sheetState,
                    availableTimesState = currentDayAvailableTimes,
                    onDismiss = { showBottomSheet = false },
                    onTimeSelected = { time ->
                        viewModel.onTimeSelected(time)
                        showBottomSheet = false
                        onDateTimeSelected()
                    }
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelect(
    sheetState: SheetState,
    availableTimesState: AvailableTimesState,
    onDismiss: () -> Unit,
    onTimeSelected: (time: LocalTime) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Escolha o Horário",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            AvailableTimesHandler(availableTimesState, onTimeSelected)
        }
    }
}

@Composable
fun AvailableTimesHandler(
    availableTimesState: AvailableTimesState,
    onTimeSelected: (time: LocalTime) -> Unit
) {
    when (availableTimesState) {
        is AvailableTimesState.Error -> {
            Text(
                text = "Ocorreu um erro ao buscar os horários disponíveis, verifique sua internet e tente novamente.",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        is AvailableTimesState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(128.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }

        is AvailableTimesState.Success -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(availableTimesState.availableTimes) { time ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                onTimeSelected(time)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = time.toHHmm(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingDateSelectPreview() {
    BookingDateSelect(onDateTimeSelected = {})
}