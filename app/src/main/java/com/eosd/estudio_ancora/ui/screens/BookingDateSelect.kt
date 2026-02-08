package com.eosd.estudio_ancora.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onBackPressed: () -> Unit = {}
) {
    var selection by remember { mutableStateOf<LocalDate?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escolha a Data e Hora") },
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val currentMonth = remember { YearMonth.now() }
            val startMonth = remember { currentMonth.minusMonths(12) }
            val endMonth = remember { currentMonth.plusMonths(12) }
            val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

            val state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = firstDayOfWeek
            )

            // Cabeçalho fixo com os dias da semana (Dom, Seg, etc.)
            DaysOfWeekTitle(daysOfWeek = remember { daysOfWeekFromLocale(firstDayOfWeek) })

            VerticalCalendar(
                state = state,
                dayContent = { day ->
                    Day(day, isSelected = selection == day.date) {
                        selection = if (selection == it.date) null else it.date
                    }
                },
                monthHeader = { month ->
                    MonthHeader(month)
                },
                modifier = Modifier.fillMaxSize()
            )
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
    val daysOfWeek = DayOfWeek.values()
    return daysOfWeek.slice(firstDayOfWeek.ordinal until daysOfWeek.size) +
            daysOfWeek.slice(0 until firstDayOfWeek.ordinal)
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookingDateSelectPreview() {
    BookingDateSelect(onBackPressed = {})
}