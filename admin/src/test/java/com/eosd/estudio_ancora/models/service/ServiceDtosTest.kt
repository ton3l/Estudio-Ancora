package com.eosd.estudio_ancora.models.service

import com.eosd.estudio_ancora.domain.Service
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceDtosTest {

    @Test
    fun serviceDocument_mapping_isCorrect() {
        val entity = Service(id = "s1", name = "Test Service", price = 15.0, duration = 2)
        val document = ServiceDocument.toDocument(entity)
        
        assertEquals(entity.id, document.id)
        assertEquals(entity.name, document.name)
        assertEquals(entity.price, document.price, 0.0)
        assertEquals(entity.duration, document.duration)
        
        val mappedBack = document.toEntity()
        assertEquals(entity, mappedBack)
    }
}
