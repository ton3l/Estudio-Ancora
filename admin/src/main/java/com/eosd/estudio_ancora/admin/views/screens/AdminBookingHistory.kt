package com.eosd.estudio_ancora.admin.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.admin.views.components.BookingFilterBottomSheet
import com.eosd.estudio_ancora.admin.views.viewModels.AdminBookingHistoryViewModel
import com.eosd.estudio_ancora.states.BookingFormState
import com.eosd.estudio_ancora.views.components.AppButton
import com.eosd.estudio_ancora.views.components.BookingSummary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBookingHistory(
    modifier: Modifier = Modifier.Companion,
    viewModel: AdminBookingHistoryViewModel = viewModel()
) {
    val bookings by viewModel.filteredBookings.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val filterFormState by viewModel.filterFormState.collectAsStateWithLifecycle()
    val serviceList by viewModel.serviceList.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Header with Filter Button
            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                AppButton(
                    modifier = Modifier.Companion
                        .height(40.dp),
                    text = "Filtrar",
                    onClick = { showBottomSheet = true },
                    leadingIcon = @Composable {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null
                        )
                    }
                )
            }

            bookings.forEach { booking ->
                BookingSummary(
                    modifier = Modifier.Companion,
                    actions = true,
                    bookingInfo = BookingFormState(
                        dateTime = booking.dateTime,
                        customer = booking.customer,
                        service = booking.service
                    ),
                    onDeleteBooking = { viewModel.deleteBooking(booking) }
                )
            }
        }

        if (showBottomSheet) {
            BookingFilterBottomSheet(
                sheetState = sheetState,
                filterState = filterFormState,
                serviceList = serviceList,
                onNameFilterChanged = viewModel::onCustomerNameFilterChanged,
                onServiceFilterChanged = viewModel::onServiceFilterChanged,
                onDateFilterChanged = viewModel::onDateFilterChanged,
                onTimeFilterChanged = viewModel::onTimeFilterChanged,
                onApplyFilters = viewModel::applyFilters,
                onClearFilters = viewModel::clearFilters,
                onDismiss = { showBottomSheet = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminBookingHistoryPreview() {
    AdminBookingHistory()
}