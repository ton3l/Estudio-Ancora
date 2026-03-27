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
}