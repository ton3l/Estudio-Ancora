package com.eosd.estudio_ancora.admin.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eosd.estudio_ancora.admin.views.components.GenericDisplayEntity

@Composable
fun AdminTimes(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val daysOfWeek = listOf(
                "Domingo",
                "Segunda-feira",
                "Terça-feira",
                "Quarta-feira",
                "Quinta-feira",
                "Sexta-feira",
                "Sábado"
            )

            daysOfWeek.forEach { day ->
                GenericDisplayEntity(
                    title = day,
                    onEditClick = { /* TODO: handle edit */ },
                    onDeleteClick = { /* TODO: handle delete/clear */ }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminTimesPreview() {
    AdminTimes()
}
