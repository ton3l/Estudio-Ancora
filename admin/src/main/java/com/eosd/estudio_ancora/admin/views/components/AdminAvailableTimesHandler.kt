package com.eosd.estudio_ancora.admin.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eosd.estudio_ancora.states.AvailableTimesState
import com.eosd.estudio_ancora.views.utils.toHHmm
import java.time.LocalTime

@Composable
fun AdminAvailableTimesHandler(
    availableTimesState: AvailableTimesState,
    onTimeSelected: (time: LocalTime) -> Unit
) {
    when (availableTimesState) {
        is AvailableTimesState.Error -> {
            Text(
                text = "Ocorreu um erro ao buscar os horários, verifique sua internet e tente novamente.",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        is AvailableTimesState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(128.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }

        is AvailableTimesState.Success -> {
            val allTimes = availableTimesState.availableTimes.toList()

            if (allTimes.isEmpty()) {
                Text(
                    text = "Não há horários para esta data.",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(allTimes) { (time, isAvailable) ->
                        val backgroundColor =
                            if (isAvailable)
                                MaterialTheme.colorScheme.surface
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        val contentAlpha = if (isAvailable) 1f else 0.5f

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(backgroundColor)
                                .border(
                                    width = 1.dp,
                                    color = if (isAvailable)
                                        MaterialTheme.colorScheme.outlineVariant
                                    else
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .alpha(contentAlpha)
                                .clickable(enabled = isAvailable) {
                                    onTimeSelected(time)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = time.toHHmm() + if (!isAvailable) " (Indisponível)" else "",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = if (isAvailable)
                                    MaterialTheme.colorScheme.onSurface
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
