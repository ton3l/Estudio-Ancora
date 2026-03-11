package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.views.components.BookingSummary
import com.eosd.estudio_ancora.views.components.PhoneNumberField
import com.eosd.estudio_ancora.views.viewModels.BookingViewModel
import com.eosd.estudio_ancora.views.components.SelectService

@Composable
fun BookingForm(
    modifier: Modifier,
    viewModel: BookingViewModel = viewModel(),
    onServiceBooked: () -> Unit = {},
) {
    val selectedDate by viewModel.selectedDay.collectAsStateWithLifecycle()
    val selectedTime by viewModel.selectedTime.collectAsStateWithLifecycle()
    val selectedService by viewModel.selectedService.collectAsStateWithLifecycle()
    val clientName by viewModel.customerName.collectAsStateWithLifecycle()
    val clientPhoneNumber by viewModel.customerPhoneNumber.collectAsStateWithLifecycle()
    val serviceList by viewModel.serviceList.collectAsStateWithLifecycle()
    val bookingInfo by viewModel.bookingInfo.collectAsStateWithLifecycle() // TODO: handle null assertion

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = clientName,
                onValueChange = { viewModel.onCustomerNameChanged(it) },
                label = { Text("Nome do Cliente") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            PhoneNumberField(
                clientPhoneNumber = clientPhoneNumber
            ) { viewModel.onCustomerPhoneNumberChanged(it) }
            SelectService(
                serviceList = serviceList,
                selectedService = selectedService
            ) { viewModel.onServiceSelected(it) }
            BookingSummary(
                actions = false,
                modifier = Modifier
                    .padding(vertical = 16.dp),
                bookingInfo = bookingInfo
            )
            Button(
                onClick = {
                    onServiceBooked()
                },
                modifier = Modifier,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Confirmar Agendamento")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingFormPreview() {
    BookingForm(modifier = Modifier)
}
