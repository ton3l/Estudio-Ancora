package com.eosd.estudio_ancora.admin.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.states.BookingFormState
import com.eosd.estudio_ancora.views.components.AppButton
import com.eosd.estudio_ancora.views.components.BookingSummary
import java.time.LocalDateTime

@Composable
fun BookingHistory(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Header with Filter Button
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(
                    modifier = Modifier
                        .height(40.dp),
                    text = "Filtrar",
                    onClick = { /* TODO: Open Filter Bottom Sheet */ },
                    leadingIcon = @Composable {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null
                        )
                    }
                )
            }

            // Mock List of BookingSummaries
            val mockBookings = listOf(
                BookingFormState(
                    dateTime = LocalDateTime.now().plusDays(1),
                    customer = Customer(name = "João Silva", phoneNumber = "11987654321"),
                    service = Service(
                        id = "1",
                        name = "Corte de Cabelo",
                        duration = 30,
                        price = 50.0
                    )
                ),
                BookingFormState(
                    dateTime = LocalDateTime.now().plusDays(2),
                    customer = Customer(name = "Maria Souza", phoneNumber = "11912345678"),
                    service = Service(id = "2", name = "Barba", duration = 20, price = 30.0)
                )
            )

            mockBookings.forEach { bookingInfo ->
                BookingSummary(
                    modifier = Modifier,
                    actions = true,
                    bookingInfo = bookingInfo,
                    onDeleteBooking = { /* TODO */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingHistoryPreview() {
    BookingHistory()
}
