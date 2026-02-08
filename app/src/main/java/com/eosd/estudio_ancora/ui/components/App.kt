package com.eosd.estudio_ancora.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eosd.estudio_ancora.ui.screens.BookingForm
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.ui.screens.BookingLog

@Composable
fun App() {
    Scaffold(
        topBar = {
            AppHeader(modifier = Modifier)
        }
    ) { innerPadding ->
        val mod = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 32.dp);
        BookingForm(
            modifier = mod
        )
//        BookingLog(
//            modifier = mod
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}