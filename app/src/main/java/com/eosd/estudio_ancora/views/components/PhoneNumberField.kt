package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class BrPhoneNumberVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text

        var out = if (trimmed.isNotEmpty()) "(" else ""

        for (i in trimmed.indices) {
            if (i == 2) out += ") "
            if (i == 7) out += "-"
            out += trimmed[i]
        }
        return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
    }

    private val phoneNumberOffsetTranslator = object : OffsetMapping {

        override fun originalToTransformed(offset: Int): Int =
            when (offset) {
                0 -> 0
                in 1..2 -> offset + 1
                in 3..7 -> offset + 3
                else -> offset + 4
            }

        override fun transformedToOriginal(offset: Int): Int =
            when (offset) {
                0 -> 0
                in 1..3 -> offset - 1
                4 -> 2
                in 5..10 -> offset - 3
                else -> offset - 4
            }
    }
}

@Composable
fun PhoneNumberField(modifier: Modifier = Modifier) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    val numericRegex = Regex("[^0-9]")
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = phoneNumber,
        onValueChange = {
            // Remove non-numeric characters.
            val stripped = numericRegex.replace(it, "")
            phoneNumber = if (stripped.length >= 11) {
                stripped.substring(0..10)
            } else {
                stripped
            }
        },
        label = { Text("Número de Telefone") },
        visualTransformation = BrPhoneNumberVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Preview(showBackground = true)
@Composable
fun PhoneNumberFieldPreview() {
    PhoneNumberField(modifier = Modifier.padding(16.dp))
}