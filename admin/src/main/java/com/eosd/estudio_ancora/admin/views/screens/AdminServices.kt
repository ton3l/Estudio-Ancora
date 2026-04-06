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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eosd.estudio_ancora.admin.views.components.AdminServiceBottomSheet
import com.eosd.estudio_ancora.admin.views.components.GenericDisplayEntity
import com.eosd.estudio_ancora.admin.views.viewModels.AdminServicesViewModel
import com.eosd.estudio_ancora.views.components.TextInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminServices(
    modifier: Modifier = Modifier,
    viewModel: AdminServicesViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredServices by viewModel.filteredServices.collectAsStateWithLifecycle()
    val serviceFormState by viewModel.serviceFormState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true )
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
                        onValueChanged = { viewModel.onSearchQueryChanged(it) }
                    )
                }
               
                IconButton(
                    onClick = {
                        viewModel.openServiceForm(null)
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
                filteredServices.forEach { service ->
                    GenericDisplayEntity(
                        title = service.name,
                        onEditClick = {
                            viewModel.openServiceForm(service)
                            bottomSheetTitle = "Editar Serviço"
                            showBottomSheet = true
                        },
                        onDeleteClick = { viewModel.deleteService(service.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
        
        if (showBottomSheet) {
            AdminServiceBottomSheet(
                title = bottomSheetTitle,
                sheetState = sheetState,
                formState = serviceFormState,
                onNameChanged = viewModel::onNameChanged,
                onPriceChanged = viewModel::onPriceChanged,
                onDurationChanged = viewModel::onDurationChanged,
                onSaveClick = {
                    viewModel.saveService(onSuccess = {
                        showBottomSheet = false
                    })
                },
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
