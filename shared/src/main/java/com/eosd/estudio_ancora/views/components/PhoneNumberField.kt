package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.views.utils.BrPhoneNumberVisualTransformation


@Composable
fun PhoneNumberField(
    modifier: Modifier = Modifier,
    customerPhoneNumber: String,
    phoneNumberError: String?,
    onPhoneNumberChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = customerPhoneNumber,
        onValueChange = {
            onPhoneNumberChanged(it)
        },
        label = { Text("Número de Telefone") },
        visualTransformation = BrPhoneNumberVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone Icon"
            )
        },
        isError = phoneNumberError != null,
        supportingText = {
            phoneNumberError?.let { errorText ->
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PhoneNumberFieldPreview() {
    PhoneNumberField(
        modifier = Modifier.padding(16.dp),
        customerPhoneNumber = "99999999999",
        null
    ) {}
}
