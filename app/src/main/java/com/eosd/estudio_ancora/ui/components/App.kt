package com.eosd.estudio_ancora.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.ui.screens.BookingDateSelect
import com.eosd.estudio_ancora.ui.screens.BookingLog

@Composable
fun App() {
    Scaffold(
        topBar = {
            AppHeader()
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
            ) {
                Nav(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
//        BookingLog(
//            modifier = Modifier
//                .padding(top = innerPadding.calculateTopPadding())
//                .padding(top = 16.dp)
//                .padding(horizontal = 16.dp),
//        )
//        BookingForm(
//            modifier = Modifier
//                .padding(innerPadding)
//                .padding(horizontal = 16.dp, vertical = 32.dp)
//        )
        BookingDateSelect(innerPadding)
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}