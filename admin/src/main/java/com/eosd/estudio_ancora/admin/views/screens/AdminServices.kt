package com.eosd.estudio_ancora.admin.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eosd.estudio_ancora.admin.views.components.AdminServiceBottomSheet
import com.eosd.estudio_ancora.admin.views.components.GenericDisplayEntity
import com.eosd.estudio_ancora.views.components.TextInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminServices(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var bottomSheetTitle by rememberSaveable { mutableStateOf("") }

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
                    onClick = {
                        bottomSheetTitle = "Adicionar Serviço"
                        showBottomSheet = true
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
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
                        onEditClick = {
                            bottomSheetTitle = "Editar Serviço"
                            showBottomSheet = true
                        },
                        onDeleteClick = { /* TODO: Delete Service */ }
                    )
                    HorizontalDivider()
                }
            }
        }
        
        if (showBottomSheet) {
            AdminServiceBottomSheet(
                title = bottomSheetTitle,
                sheetState = sheetState,
                onDismiss = { showBottomSheet = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminServicesPreview() {
    AdminServices()
}
