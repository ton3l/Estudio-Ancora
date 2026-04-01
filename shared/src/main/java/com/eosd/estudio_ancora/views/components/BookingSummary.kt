package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.views.utils.BrPhoneNumberVisualTransformation
import com.eosd.estudio_ancora.views.utils.toCurrency
import com.eosd.estudio_ancora.views.utils.toPtBrSplitText
import com.eosd.estudio_ancora.states.BookingFormState
import java.time.LocalDateTime

@Composable
fun BookingSummary(
    modifier: Modifier,
    actions: Boolean = false,
    onDeleteBooking: () -> Unit = {},
    bookingInfo: BookingFormState
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .width(width = 320.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CardHeader(actions, bookingInfo.dateTime) { onDeleteBooking() }
            CardBody(bookingInfo.customer, bookingInfo.service?.name ?: "")
            CardFooter(actions, bookingInfo.service?.price ?: 0.0)
        }
    }
}

@Composable
fun CardHeader(actions: Boolean, bookingDateTime: LocalDateTime, onDeleteBooking: () -> Unit) {
    val (dateText, timeText) = bookingDateTime.toPtBrSplitText()
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(40.dp),
            imageVector = Icons.Filled.DateRange,
            contentDescription = "Ícone de agendamento"
        )
        Column(
            modifier = Modifier
                .padding(top = 4.dp),
        ) {
            Text(
                text = dateText,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
            Text(
                text = timeText,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
        }
        Icon(
            modifier = if (!actions) Modifier
                .alpha(0f)
            else Modifier
                .size(size = 26.dp)
                .clickable(
                    onClick = {
                        showDialog = true
                    }
                ),
            imageVector = Icons.Filled.Close,
            contentDescription = "Apagar agendamento",
        )
        ConfirmDeleteModal(showDialog, { showDialog = false }) { onDeleteBooking() }
    }
}

@Composable
fun CardBody(customer: Customer, serviceName: String = "") {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = customer.name,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
            Text(
                text = BrPhoneNumberVisualTransformation.filter(customer.phoneNumber),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
        Text(
            text = serviceName,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CardFooter(actions: Boolean, servicePrice: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = servicePrice.toCurrency(),
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
    }
}

@Composable
fun ConfirmDeleteModal(showDialog: Boolean, onDismissRequest: () -> Unit, onConfirm: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                TextButton(onClick = { onConfirm(); onDismissRequest() }) { Text("Sim") }
            },
            dismissButton = {
                TextButton(onClick = { onDismissRequest() }) { Text("Não") }
            },
            title = { Text("Deseja cancelar o agendamento?") },
        )
    }

}

@Preview(showBackground = true)
@Composable
fun BookingSummaryPreview() {
    BookingSummary(
        actions = true, modifier = Modifier.padding(16.dp),
        bookingInfo = BookingFormState(
            dateTime = LocalDateTime.parse("2024-06-30T14:30:00"),
            customer = Customer(
                name = "João Silva",
                phoneNumber = "11987654321"
            ),
            service = Service(
                id = "1",
                name = "Corte de Cabelo",
                duration = 60,
                price = 50.0
            )
        )
    )
}
