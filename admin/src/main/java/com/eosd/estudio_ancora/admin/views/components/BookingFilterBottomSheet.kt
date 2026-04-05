package com.eosd.estudio_ancora.admin.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.states.BookingFilterState
import com.eosd.estudio_ancora.views.components.SelectService
import com.eosd.estudio_ancora.views.components.TextInput
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFilterBottomSheet(
    sheetState: SheetState,
    filterState: BookingFilterState,
    serviceList: List<Service>,
    onNameFilterChanged: (String) -> Unit,
    onServiceFilterChanged: (Service?) -> Unit,
    onDateFilterChanged: (LocalDate?) -> Unit,
    onTimeFilterChanged: (String) -> Unit,
    onApplyFilters: () -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    // Limpar o foco ao abrir a folha para evitar que o TimeInput ganhe foco automático
    LaunchedEffect(Unit) {
        focusManager.clearFocus()
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { 
                onClearFilters()
                onDismiss()
            }) {
                Text("Limpar")
            }
            Text(
                text = "Filtrar",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            TextButton(onClick = { 
                onApplyFilters()
                onDismiss()
            }) {
                Text("Aplicar")
            }
        }
        
        Column(
            modifier = Modifier
                .padding(horizontal = 26.dp, vertical = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextInput(
                value = filterState.customerName,
                valueError = null,
                label = "Nome do Cliente",
                leadingIcon = Icons.Default.Person,
                onValueChanged = onNameFilterChanged
            )

            SelectService(
                serviceList = serviceList,
                selectedService = filterState.service,
                serviceError = null,
                onServiceSelected = onServiceFilterChanged
            )

            InlineDatePicker(
                selectedDateMillis = filterState.date?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
                onDateSelected = { millis ->
                    val date = millis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    onDateFilterChanged(date)
                }
            )
            
            TextInput(
                value = filterState.time,
                valueError = null,
                label = "Horário",
                leadingIcon = Icons.Default.Schedule,
                onValueChanged = onTimeFilterChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(184.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BookingFilterBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()
    Column() {
        BookingFilterBottomSheet(
            sheetState = sheetState,
            filterState = BookingFilterState(),
            serviceList = emptyList(),
            onNameFilterChanged = {},
            onServiceFilterChanged = {},
            onDateFilterChanged = {},
            onTimeFilterChanged = {},
            onApplyFilters = {},
            onClearFilters = {},
            onDismiss = {}
        )
    }
}
