package com.eosd.estudio_ancora.services

import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.models.service.ServiceModel
import com.eosd.estudio_ancora.services.utils.generateFirestoreId
import com.eosd.estudio_ancora.states.ServiceFormState

object ServiceService {
    suspend fun getAllServices(): List<Service> {
        return ServiceModel.getAllServices()
    }

    suspend fun saveService(state: ServiceFormState) {
        val isNew = state.id == null || state.id.isBlank()
        val serviceId = if (isNew) generateFirestoreId() else state.id!!
        
        // In the form, price is represented as cents (e.g., "5000" for 50.00).
        // We divide by 100.0 to store the correct Double value.
        val priceCents = state.price.toDoubleOrNull() ?: 0.0
        val service = Service(
            id = serviceId,
            name = state.name.trim(),
            price = priceCents / 100.0,
            duration = state.duration.toIntOrNull() ?: 0
        )
        
        if (isNew) {
            ServiceModel.addService(service)
        } else {
            ServiceModel.updateService(service)
        }
    }

    suspend fun deleteService(serviceId: String) {
        ServiceModel.deleteService(serviceId)
    }
}