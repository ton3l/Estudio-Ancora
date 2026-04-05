package com.eosd.estudio_ancora.admin.views.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.services.ServiceService
import com.eosd.estudio_ancora.states.ServiceFormState
import com.eosd.estudio_ancora.validators.ServiceValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminServicesViewModel : ViewModel() {

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredServices: StateFlow<List<Service>> = combine(
        _services,
        _searchQuery
    ) { servicesList, query ->
        if (query.isBlank()) {
            servicesList
        } else {
            servicesList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _serviceFormState = MutableStateFlow(ServiceFormState())
    val serviceFormState: StateFlow<ServiceFormState> = _serviceFormState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchServices()
    }

    fun fetchServices() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _services.value = ServiceService.getAllServices()
            } catch (e: Exception) {
                // Ignore for now, might want to show error state later
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onNameChanged(name: String) {
        _serviceFormState.update { currentState ->
            ServiceValidator.validate(currentState.copy(name = name))
        }
    }

    fun onPriceChanged(price: String) {
        _serviceFormState.update { currentState ->
            ServiceValidator.validate(currentState.copy(price = price))
        }
    }

    fun onDurationChanged(duration: String) {
        _serviceFormState.update { currentState ->
            ServiceValidator.validate(currentState.copy(duration = duration))
        }
    }

    fun openServiceForm(service: Service?) {
        if (service == null) {
            _serviceFormState.value = ServiceFormState()
        } else {
            // Price is stored as Double (e.g., 50.0). We need to convert it back to cents (e.g., "5000")
            // for the input field which uses CurrencyVisualTransformation.
            val priceInCents = (service.price * 100).toLong().toString()
            
            _serviceFormState.value = ServiceFormState(
                id = service.id,
                name = service.name,
                price = priceInCents,
                duration = service.duration.toString(),
                isFormValid = true // Already valid since it came from the DB
            )
        }
    }

    fun saveService(onSuccess: () -> Unit) {
        if (!_serviceFormState.value.isFormValid) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                ServiceService.saveService(_serviceFormState.value)
                fetchServices()
                onSuccess()
            } catch (e: Exception) {
                // Ignore for now
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteService(serviceId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                ServiceService.deleteService(serviceId)
                fetchServices()
            } catch (e: Exception) {
                // Ignore
            } finally {
                _isLoading.value = false
            }
        }
    }
}
