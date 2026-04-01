package com.eosd.estudio_ancora.admin.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.admin.views.components.GenericDisplayEntity
import com.eosd.estudio_ancora.views.components.TextInput

@Composable
fun AdminServices(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header: Search Bar + Add Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                     TextInput(
                        value = searchQuery,
                        valueError = null,
                        label = "Pesquisar",
                        leadingIcon = Icons.Default.Search,
                        onValueChanged = { searchQuery = it }
                    )
                }
               
                IconButton(
                    onClick = { /* TODO: Add new service */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Serviço"
                    )
                }
            }

            HorizontalDivider()

            // List of Services
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                val mockServices = listOf(
                    "Corte de Cabelo",
                    "Barba",
                    "Sobrancelha",
                    "Corte e Barba",
                    "Platinado",
                    "Luzes"
                )

                mockServices.forEach { serviceName ->
                    GenericDisplayEntity(
                        title = serviceName,
                        onEditClick = { /* TODO: Edit Service */ },
                        onDeleteClick = { /* TODO: Delete Service */ }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminServicesPreview() {
    AdminServices()
}
