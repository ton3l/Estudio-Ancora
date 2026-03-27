package com.eosd.estudio_ancora.models.service

import com.eosd.estudio_ancora.domain.Service
import com.google.firebase.firestore.DocumentId

data class ServiceDocument(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val duration: Int = 0
) {
    fun toEntity(): Service{
        return Service(
            id = this.id,
            name = this.name,
            price = this.price,
            duration = this.duration
        )
    }

    companion object {
        fun toDocument(service: Service): ServiceDocument {
            return ServiceDocument(
                id = service.id,
                name = service.name,
                price = service.price,
                duration = service.duration
            )
        }
    }
}
