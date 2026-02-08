package com.eosd.estudio_ancora.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.ui.components.BookingSummary

@Composable
fun BookingLog(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        BookingSummary(modifier = Modifier, actions = true)
        BookingSummary(modifier = Modifier, actions = true)
        BookingSummary(modifier = Modifier, actions = true)
        BookingSummary(modifier = Modifier, actions = true)
        BookingSummary(modifier = Modifier, actions = true)
        BookingSummary(modifier = Modifier, actions = true)
    }
}

@Preview(showBackground = true)
@Composable
fun BookingLogPreview() {
    BookingLog(modifier = Modifier)
}
