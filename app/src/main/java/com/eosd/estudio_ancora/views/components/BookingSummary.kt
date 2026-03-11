package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eosd.estudio_ancora.domain.Customer
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.views.interfaces.BookingInfo
import com.eosd.estudio_ancora.views.utils.BrPhoneNumberVisualTransformation
import com.eosd.estudio_ancora.views.utils.toCurrency
import com.eosd.estudio_ancora.views.utils.toPtBrSplitText
import java.time.LocalDateTime

@Composable
fun BookingSummary(
    modifier: Modifier,
    actions: Boolean = false,
    bookingInfo: BookingInfo
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
            CardHeader(actions, bookingInfo.dateTime)
            CardBody(bookingInfo.customer, bookingInfo.service.name)
            CardFooter(actions, bookingInfo.service.price)
        }
    }
}

@Composable
fun CardHeader(actions: Boolean, bookingDateTime: LocalDateTime) {
    val (dateText, timeText) = bookingDateTime.toPtBrSplitText()

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
                .size(size = 26.dp),
            imageVector = Icons.Filled.Close,
            contentDescription = "Apagar agendamento"
        )
    }
}

@Composable
fun CardBody(customer: Customer, serviceName: String) {
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
        Button(
            onClick = { false },
            modifier = if (!actions) Modifier
                .alpha(0f)
            else Modifier,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Text("Editar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingSummaryPreview() {
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
                price = 25.0
            )
        }
    )
}