package com.eosd.estudio_ancora.views.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object BrPhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text

        var formattedText = if (trimmed.isNotEmpty()) "(" else ""

        for (i in trimmed.indices) {
            if (i == 2) formattedText += ") "
            if (i == 7) formattedText += "-"
            formattedText += trimmed[i]
        }
        return TransformedText(AnnotatedString(formattedText), phoneNumberOffsetTranslator)
    }

    fun filter(text: String): String {
        val trimmed = if (text.length >= 11) text.substring(0..10) else text

        var formattedText = if (trimmed.isNotEmpty()) "(" else ""

        for (i in trimmed.indices) {
            if (i == 2) formattedText += ") "
            if (i == 7) formattedText += "-"
            formattedText += trimmed[i]
        }

        return formattedText
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