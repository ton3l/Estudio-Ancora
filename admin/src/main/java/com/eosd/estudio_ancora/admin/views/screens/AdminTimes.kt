package com.eosd.estudio_ancora.admin.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.admin.views.components.AdminTimesBottomSheet
import com.eosd.estudio_ancora.admin.views.components.GenericDisplayEntity
import com.eosd.estudio_ancora.admin.views.viewModels.AdminTimesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTimes(
    modifier: Modifier = Modifier,
    viewModel: AdminTimesViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    val weekRules by viewModel.weekRules.collectAsStateWithLifecycle()
    val selectedDayRule by viewModel.selectedDayRule.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            weekRules.forEach { rule ->
                val dayName = viewModel.dayTranslations[rule.weekDay] ?: rule.weekDay
                GenericDisplayEntity(
                    title = dayName,
                    canDelete = false,
                    onEditClick = {
                        viewModel.selectDay(rule.weekDay)
                        showBottomSheet = true
                    },
                    onDeleteClick = { }
                )
                HorizontalDivider()
            }

            Button(
                onClick = { viewModel.updateCurrentWeek() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Atualizando..." else "Atualizar horários da semana atual")
            }
        }

        if (showBottomSheet && selectedDayRule != null) {
            AdminTimesBottomSheet(
                dayName = viewModel.dayTranslations[selectedDayRule!!.weekDay] ?: selectedDayRule!!.weekDay,
                isOpen = selectedDayRule!!.open,
                timeSlots = selectedDayRule!!.timeSlots,
                onToggleOpen = viewModel::toggleDayOpen,
                onToggleTimeSlot = viewModel::toggleTimeSlot,
                onSave = {
                    viewModel.saveSelectedDay {
                        showBottomSheet = false
                    }
                },
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
