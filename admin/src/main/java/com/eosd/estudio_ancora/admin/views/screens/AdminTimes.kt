package com.eosd.estudio_ancora.admin.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eosd.estudio_ancora.admin.views.components.AdminTimesBottomSheet
import com.eosd.estudio_ancora.admin.views.components.GenericDisplayEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTimes(
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedDay by rememberSaveable { mutableStateOf("") }

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
                    onEditClick = {
                        selectedDay = day
                        showBottomSheet = true
                    },
                    onDeleteClick = { /* TODO: handle delete/clear */ }
                )
                HorizontalDivider()
            }
        }

        if (showBottomSheet) {
            AdminTimesBottomSheet(
                dayName = selectedDay,
                sheetState = sheetState,
                onDismiss = { showBottomSheet = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminTimesPreview() {
    AdminTimes()
}
