package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.eosd.estudio_ancora.views.viewModels.BookingViewModel
import com.eosd.estudio_ancora.views.viewModels.states.AvailableTimesState
import com.eosd.estudio_ancora.views.components.Calendar
import com.eosd.estudio_ancora.views.utils.toHHmm
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDateSelect(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: BookingViewModel = viewModel(),
    onDateTimeSelected: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val selectedDay by viewModel.selectedDay.collectAsStateWithLifecycle()
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
            Calendar(selectedDay = selectedDay) {
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