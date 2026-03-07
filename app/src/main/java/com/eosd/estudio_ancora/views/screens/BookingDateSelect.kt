package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.eosd.estudio_ancora.views.components.Routes
import com.eosd.estudio_ancora.views.viewModels.BookingDateSelectViewModel
import com.eosd.estudio_ancora.views.viewModels.states.AvailableTimesState
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDateSelect(
    paddingValues: PaddingValues = PaddingValues(),
    navController: NavController,
    viewModel: BookingDateSelectViewModel = viewModel(),
    onBackPressed: () -> Unit = {}
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
                    .padding(top = paddingValues.calculateTopPadding() - 24.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val currentMonth = remember { YearMonth.now() }
            val startMonth = remember { currentMonth.minusMonths(0) }
            val endMonth = remember { currentMonth.plusMonths(6) }
            val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
            val today = remember { LocalDate.now() }

            val state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = firstDayOfWeek
            )

            DaysOfWeekTitle(daysOfWeek = remember { daysOfWeekFromLocale(firstDayOfWeek) })

            VerticalCalendar(
                state = state,
                dayContent = { day ->
                    Day(day, isSelected = (selectedDay == day.date), today = today) {
                        viewModel.onDaySelected(day.date)
                        showBottomSheet = true
                    }
                },
                monthHeader = { month ->
                    MonthHeader(month)
                },
                modifier = Modifier.fillMaxSize()
            )

            if (showBottomSheet)
                TimeSelect(
                    navController = navController,
                    sheetState = sheetState,
                    availableTimesState = currentDayAvailableTimes
                ) { showBottomSheet = false }
        }
    }
}

@Composable
fun MonthHeader(calendarMonth: CalendarMonth) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "${
                calendarMonth.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { it.uppercase() }
            } ${calendarMonth.yearMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun daysOfWeekFromLocale(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
    val daysOfWeek = DayOfWeek.entries.toTypedArray()
    return daysOfWeek.slice(firstDayOfWeek.ordinal until daysOfWeek.size) +
            daysOfWeek.slice(0 until firstDayOfWeek.ordinal)
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, today: LocalDate, onClick: (CalendarDay) -> Unit) {
    val isBeforeToday = day.date.isBefore(today)
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate && !isBeforeToday,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isBeforeToday -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelect(
    navController: NavController,
    sheetState: SheetState,
    availableTimesState: AvailableTimesState,
    onDismiss: () -> Unit
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
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(64.dp)
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }

                is AvailableTimesState.Success -> {
                    for (time in availableTimesState.availableTimes) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        navController.navigate(Routes.BOOKING_FORM)
                                    }
                                )
                                .height(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${time.hour}",
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingDateSelectPreview() {
    BookingDateSelect(navController = rememberNavController())
}