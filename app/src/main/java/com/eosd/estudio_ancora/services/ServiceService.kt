package com.eosd.estudio_ancora.services

import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.models.service.ServiceModel

object ServiceService {
    suspend fun getAllServices(): List<Service> {
        return ServiceModel.getAllServices()
    }
}