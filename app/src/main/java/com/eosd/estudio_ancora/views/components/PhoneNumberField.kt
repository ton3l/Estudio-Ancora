package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.views.utils.BrPhoneNumberVisualTransformation


@Composable
fun PhoneNumberField(modifier: Modifier = Modifier, clientPhoneNumber: String, onPhoneNumberChanged: (String) -> Unit) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = clientPhoneNumber,
        onValueChange = {
            onPhoneNumberChanged(it)
        },
        label = { Text("Número de Telefone") },
        visualTransformation = BrPhoneNumberVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Preview(showBackground = true)
@Composable
fun PhoneNumberFieldPreview() {
    PhoneNumberField(modifier = Modifier.padding(16.dp), clientPhoneNumber = "99999999999") {}
}