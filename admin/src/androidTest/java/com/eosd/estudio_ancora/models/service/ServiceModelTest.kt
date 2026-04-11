package com.eosd.estudio_ancora.models.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.domain.Service
import com.eosd.estudio_ancora.libs.firestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ServiceModelTest {
    private val servicesCollection = firestore.collection("services")
    private val testServices = listOf(
        Service(id = "test-service-1", name = "Test Service 1", price = 10.0, duration = 1),
        Service(id = "test-service-2", name = "Test Service 2", price = 20.0, duration = 2)
    )
    private val additionalTestIds = mutableListOf<String>()

    @Before
    fun setUp() {
        runBlocking {
            // Clean and seed
            additionalTestIds.clear()
            cleanUp()
            for (service in testServices) {
                servicesCollection.document(service.id).set(ServiceDocument.toDocument(service)).await()
            }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            cleanUp()
        }
    }

    private suspend fun cleanUp() {
        // Clean fixed test services
        val snapshot = servicesCollection.whereIn("id", testServices.map { it.id }).get().await()
        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }

        // Clean additional test services created during tests
        additionalTestIds.forEach { id ->
            servicesCollection.document(id).delete().await()
        }
        additionalTestIds.clear()
    }

    @Test
    fun getAllServices_returnsAllPersistedServices() {
        runBlocking {
            val services = ServiceModel.getAllServices()

            val returnedTestServices = services.filter { it.id.startsWith("test-service-") }
            assertEquals("Should return the seeded test services", 2, returnedTestServices.size)

            val s1 = returnedTestServices.find { it.id == "test-service-1" }
            assertEquals("Test Service 1", s1?.name)
            assertEquals(10.0, s1?.price!!, 0.0)
            assertEquals(1, s1?.duration)

            val s2 = returnedTestServices.find { it.id == "test-service-2" }
            assertEquals("Test Service 2", s2?.name)
            assertEquals(20.0, s2?.price!!, 0.0)
            assertEquals(2, s2?.duration)
        }
    }

    @Test
    fun addService_persistsNewService() {
        runBlocking {
            val newServiceId = "test-add-service"
            additionalTestIds.add(newServiceId)
            val newService = Service(id = newServiceId, name = "Added Service", price = 30.0, duration = 3)
            ServiceModel.addService(newService)

            val retrieved = servicesCollection.document(newService.id).get().await().toObject(ServiceDocument::class.java)
            assertNotNull(retrieved)
            assertEquals("Added Service", retrieved?.name)
            assertEquals(30.0, retrieved?.price!!, 0.0)
        }
    }

    @Test
    fun updateService_modifiesExistingService() {
        runBlocking {
            val updatedService = testServices[0].copy(name = "Updated Name", price = 99.0)
            ServiceModel.updateService(updatedService)

            val retrieved = servicesCollection.document(testServices[0].id).get().await().toObject(ServiceDocument::class.java)
            assertEquals("Updated Name", retrieved?.name)
            assertEquals(99.0, retrieved?.price!!, 0.0)
        }
    }

    @Test
    fun deleteService_removesServiceFromFirestore() {
        runBlocking {
            ServiceModel.deleteService(testServices[1].id)

            val retrieved = servicesCollection.document(testServices[1].id).get().await()
            assertFalse(retrieved.exists())
        }
    }
}
