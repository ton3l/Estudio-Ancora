package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.views.components.BookingSummary
import com.eosd.estudio_ancora.views.interfaces.BookingInfo
import com.eosd.estudio_ancora.views.viewModels.BookingHistoryViewModel
import com.eosd.estudio_ancora.views.viewModels.states.ActiveBookingsState
import java.time.LocalDateTime

@Composable
fun BookingHistory(
    modifier: Modifier,
    viewModel: BookingHistoryViewModel = viewModel()
) {
    val activeBookingsState by viewModel.bookings.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchBookings()
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        ActiveBookingsHandler(activeBookingsState = activeBookingsState)
    }
}

@Composable
fun ActiveBookingsHandler(activeBookingsState: ActiveBookingsState) {
    when (activeBookingsState) {
        is ActiveBookingsState.Error -> {}
        is ActiveBookingsState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }

        is ActiveBookingsState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                activeBookingsState.bookings.forEach { booking ->
                    BookingSummary(
                        actions = true,
                        modifier = Modifier.padding(16.dp),
                        bookingInfo = object : BookingInfo {
                            override val dateTime: LocalDateTime = booking.dateTime
                            override val customer: Customer = booking.customer
                            override val service: Service = booking.service
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingHistoryPreview() {
    BookingHistory(modifier = Modifier)
}
