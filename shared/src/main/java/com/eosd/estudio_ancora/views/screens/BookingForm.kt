package com.eosd.estudio_ancora.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.states.BookingState
import com.eosd.estudio_ancora.views.components.BookingSummary
import com.eosd.estudio_ancora.views.components.PhoneNumberField
import com.eosd.estudio_ancora.views.components.SelectService
import com.eosd.estudio_ancora.views.components.TextInput
import com.eosd.estudio_ancora.views.viewModels.BookingViewModel

@Composable
fun BookingForm(
    modifier: Modifier,
    viewModel: BookingViewModel = viewModel(),
    onSubmit: () -> Unit = {},
) {
    val serviceList by viewModel.serviceList.collectAsStateWithLifecycle()
    val bookingFormState by viewModel.bookingFormState.collectAsStateWithLifecycle()
    val bookingState by viewModel.bookingState.collectAsStateWithLifecycle()

    if (bookingState is BookingState.Error) {
        AlertDialog(
            onDismissRequest = { viewModel.resetBookingState() },
            confirmButton = {
                TextButton(onClick = { viewModel.resetBookingState() }) {
                    Text("OK")
                }
            },
            title = { Text("Erro no Agendamento") },
            text = { Text((bookingState as BookingState.Error).message) }
        )
    }

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
            TextInput(
                value = bookingFormState.customer.name,
                valueError = bookingFormState.customerNameError,
                label = "Nome do Cliente",
                leadingIcon = Icons.Default.Person,
                onValueChanged = { viewModel.onCustomerNameChanged(it) }
            )
            PhoneNumberField(
                customerPhoneNumber = bookingFormState.customer.phoneNumber,
                phoneNumberError = bookingFormState.customerPhoneNumberError
            ) { viewModel.onCustomerPhoneNumberChanged(it) }
            SelectService(
                serviceList = serviceList,
                selectedService = bookingFormState.service,
                serviceError = bookingFormState.serviceError
            ) { viewModel.onServiceSelected(it) }
            BookingSummary(
                actions = false,
                modifier = Modifier
                    .padding(vertical = 16.dp),
                bookingInfo = bookingFormState
            )
            Button(
                onClick = {
                    onSubmit()
                },
                enabled = bookingState !is BookingState.Loading,
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
