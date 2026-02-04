package com.eosd.estudio_ancora.ui.components

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

@Composable
fun BookingSummary(modifier: Modifier, actions: Boolean = false) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .width(width = 320.dp)
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CardHeader(actions)
            CardBody()
            CardFooter(actions)
        }
    }
}

@Composable
fun CardHeader(actions: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(40.dp),
            imageVector = Icons.Filled.DateRange,
            contentDescription = "Ícone de agendamento"
        )
        Column(
            modifier = Modifier
                .padding(top = 4.dp),
        ) {
            Text(
                text = "Sex, 01 de Dezembro",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
            Text(
                text = "xx:xx:xx",
                textAlign = TextAlign.Center,
            )
        }
        Icon(
            modifier = if (!actions) Modifier
                .alpha(0f)
            else Modifier
                .size( size = 26.dp ) ,
            imageVector = Icons.Filled.Close,
            contentDescription = "Apagar agendamento"
        )
    }
}

@Composable
fun CardBody(){
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Nome do Cliente",
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Text(
            text = "Serviço",
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun CardFooter(actions: Boolean){
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

@Preview(showBackground = true)
@Composable
fun BookingSummaryPreview() {
    BookingSummary(actions = true, modifier = Modifier.padding(16.dp))
}