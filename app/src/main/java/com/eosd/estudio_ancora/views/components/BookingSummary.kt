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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BookingSummary(
    modifier: Modifier,
    actions: Boolean = false,
    bookingDateTime: LocalDateTime = LocalDateTime.of(1000, 12, 1, 0, 0)
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
            CardHeader(actions, bookingDateTime)
            CardBody()
            CardFooter(actions)
        }
    }
}

@Composable
fun CardHeader(actions: Boolean, bookingDateTime: LocalDateTime) {
    val ( dateText, timeText ) = dateTimeFormatter(bookingDateTime)

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
fun CardBody() {
    val clientName = "Nome do Cliente"
    val clientContact = "(xx) xxxxx-xxxx"
    val serviceName = "Serviço"
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Text(
                text = clientName,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
            Text(
                text = clientContact,
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
fun CardFooter(actions: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "R$ " + "00,00",
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

fun dateTimeFormatter(dateTime: LocalDateTime): Pair<String, String> {
    val dateFormatter = DateTimeFormatter.ofPattern(
        "EEE dd 'de' MMMM 'de' yyyy",
        Locale("pt", "BR")
    )
    val dateText = dateTime.format(dateFormatter)
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale(
                    "pt",
                    "BR"
                )
            ) else it.toString()
        }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale("pt", "BR"))
    val timeText = dateTime.format(timeFormatter)

    return Pair(dateText, timeText)
}

@Preview(showBackground = true)
@Composable
fun BookingSummaryPreview() {
    BookingSummary(actions = true, modifier = Modifier.padding(16.dp))
}