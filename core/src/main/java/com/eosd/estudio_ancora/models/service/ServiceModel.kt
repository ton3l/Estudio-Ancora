package com.eosd.estudio_ancora.models.service

import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.libs.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

object ServiceModel {
    val servicesCollection = firestore.collection("services")

    suspend fun getAllServices(): List<Service> {
        val services = servicesCollection.get().await()

        return services
            .toObjects<ServiceDocument>()
            .map { it.toEntity() }
    }

    suspend fun addService(service: Service) {
        val serviceDocument = ServiceDocument.toDocument(service)
        servicesCollection.document(service.id).set(serviceDocument).await()
    }

    suspend fun updateService(service: Service) {
        val serviceDocument = ServiceDocument.toDocument(service)
        servicesCollection.document(service.id).set(serviceDocument).await()
    }

    suspend fun deleteService(serviceId: String) {
        servicesCollection.document(serviceId).delete().await()
    }
}