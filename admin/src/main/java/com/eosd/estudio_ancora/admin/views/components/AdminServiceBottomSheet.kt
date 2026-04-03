package com.eosd.estudio_ancora.admin.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.views.components.AppButton
import com.eosd.estudio_ancora.views.components.TextInput
import com.eosd.estudio_ancora.views.utils.CurrencyVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminServiceBottomSheet(
    title: String,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    // For now we're just building the UI, so empty state is fine.
    var serviceName by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var serviceDuration by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            TextInput(
                value = serviceName,
                valueError = null,
                label = "Nome do Serviço",
                leadingIcon = Icons.Default.ContentCut,
                onValueChanged = { serviceName = it }
            )

            TextInput(
                value = servicePrice,
                valueError = null,
                label = "Preço",
                leadingIcon = Icons.Default.AttachMoney,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = CurrencyVisualTransformation(),
                onValueChanged = { newValue ->
                    // Filtra apenas dígitos decimais
                    val digits = newValue.filter { it.isDigit() }
                    servicePrice = digits
                }
            )

            TextInput(
                value = serviceDuration,
                valueError = null,
                label = "Duração em Horas",
                leadingIcon = Icons.Default.Timer,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChanged = { newValue ->
                    // Permite apenas dígitos e limita a 2 caracteres
                    val digits = newValue.filter { it.isDigit() }.take(2)
                    serviceDuration = digits
                }
            )

            AppButton(
                modifier = Modifier.width(128.dp),
                text = "Aplicar",
                onClick = { onDismiss() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AdminServiceBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()
    Column() {
        AdminServiceBottomSheet("Editar Serviço ", sheetState) { }
    }
}
