package com.eosd.estudio_ancora.admin.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eosd.estudio_ancora.views.components.AppButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTimesBottomSheet(
    dayName: String,
    isOpen: Boolean,
    timeSlots: Map<String, Boolean>,
    onToggleOpen: (Boolean) -> Unit,
    onToggleTimeSlot: (String, Boolean) -> Unit,
    onSave: () -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Special row for "Aberto"
            TimeSlotRow(
                label = "Aberto",
                isChecked = isOpen,
                onCheckedChange = { onToggleOpen(it) }
            )

            // Dynamic time slots
            if (isOpen) {
                // Ensure correct chronological order since keys are strings "HH:MM"
                val sortedSlots = timeSlots.keys.sorted()

                sortedSlots.forEach { timeStr ->
                    TimeSlotRow(
                        label = "$timeStr horas",
                        isChecked = timeSlots[timeStr] ?: false,
                        onCheckedChange = { isChecked ->
                            onToggleTimeSlot(timeStr, isChecked)
                        }
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AppButton(
                text = "Aplicar",
                onClick = onSave
            )
        }
    }
}

@Composable
fun TimeSlotRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = label,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AdminTimesBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()
    Column() {
        AdminTimesBottomSheet(
            dayName = "Segunda",
            isOpen = true,
            timeSlots = mapOf("07:00" to true, "08:00" to false),
            onToggleOpen = {},
            onToggleTimeSlot = { _, _ -> },
            onSave = {},
            sheetState = sheetState
        ) { }
    }
}
