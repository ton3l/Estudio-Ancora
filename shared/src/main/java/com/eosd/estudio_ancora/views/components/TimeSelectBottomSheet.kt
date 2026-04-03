package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eosd.estudio_ancora.states.AvailableTimesState
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectBottomSheet(
    sheetState: SheetState,
    availableTimesState: AvailableTimesState,
    onDismiss: () -> Unit,
    onTimeSelected: (time: LocalTime) -> Unit,
    availableTimesHandler: @Composable (AvailableTimesState, (LocalTime) -> Unit) -> Unit
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
            availableTimesHandler(availableTimesState, onTimeSelected)
        }
    }
}