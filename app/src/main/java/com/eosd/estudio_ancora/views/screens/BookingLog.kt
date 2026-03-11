package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.views.components.BookingSummary
import com.eosd.estudio_ancora.views.interfaces.BookingInfo
import java.time.LocalDateTime

@Composable
fun BookingLog(modifier: Modifier) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            for (i in 1..5){
                BookingSummary(
                    actions = true, modifier = Modifier.padding(16.dp),
                    bookingInfo = object : BookingInfo {
                        override val dateTime: LocalDateTime = LocalDateTime.now()
                        override val customer: Customer = Customer(
                            name = "Nome do Cliente",
                            phoneNumber = "(xx) xxxxx-xxxx"
                        )
                        override val service: Service = Service(
                            id = "1",
                            name = "Corte de Cabelo",
                            duration = 30,
                            price = 25.00
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingLogPreview() {
    BookingLog(modifier = Modifier)
}
