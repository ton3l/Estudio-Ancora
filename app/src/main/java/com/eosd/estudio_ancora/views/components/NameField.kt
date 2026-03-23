package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NameField(
    customerName: String,
    customerNameError: String?,
    onCustomerNameChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = customerName,
        onValueChange = {
            onCustomerNameChanged(it)
        },
        label = { Text("Nome do Cliente") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        isError = customerNameError != null,
        supportingText = {
            customerNameError?.let { errorText ->
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}