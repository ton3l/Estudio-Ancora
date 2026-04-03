package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    value: String,
    valueError: String?,
    label: String,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = valueError != null,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        supportingText = {
            valueError?.let { errorText ->
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
fun TextInputPreview() {
    TextInput(
        value = "Texto de teste",
        valueError = null,
        label = "Campo de Teste",
        leadingIcon = Icons.Default.Person,
        onValueChanged = {}
    )
}
