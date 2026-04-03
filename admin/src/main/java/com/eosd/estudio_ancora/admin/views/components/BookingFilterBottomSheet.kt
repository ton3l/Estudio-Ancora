package com.eosd.estudio_ancora.admin.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eosd.estudio_ancora.views.components.AppButton
import com.eosd.estudio_ancora.views.components.SelectService
import com.eosd.estudio_ancora.views.components.TextInput
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFilterBottomSheet(
    sheetState: SheetState,
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
        Text(
            text = "Filtrar os agendamentos",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 26.dp, vertical = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextInput(
                value = "",
                valueError = null,
                label = "Nome do Cliente",
                leadingIcon = Icons.Default.Person,
                onValueChanged = { }
            )

            SelectService(
                serviceList = emptyList(), // Mock empty list for now
                selectedService = null,
                serviceError = null,
                onServiceSelected = { }
            )

            InlineDatePicker()
            FilterTimeInput()

            AppButton(
                text = "Aplicar",
                onClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTimeInput() {
    val currentTime = Calendar.getInstance()

    var allowFocus by remember { mutableStateOf(false) }

    // Enable focus after composition is complete to prevent autofocus
    LaunchedEffect(Unit) {
        // Wait for composition to complete before allowing focus
        kotlinx.coroutines.delay(200)
        allowFocus = true
    }

    val timePickerState = rememberTimePickerState(
        is24Hour = true,
    )

    TimeInput(
        state = timePickerState,
        modifier = Modifier
            .padding(top = 24.dp)
            .focusProperties() {
                canFocus = allowFocus
            },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BookingFilterBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()
    Column() {
        BookingFilterBottomSheet(sheetState) { }
    }
}
