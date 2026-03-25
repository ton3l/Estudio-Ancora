package com.eosd.estudio_ancora

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ServiceSeederTest {

    private val db = Firebase.firestore
    private val servicesCollection = db.collection("services")

    @Test
    fun a_seedServices() = runBlocking {
        println("=== INICIANDO SEED DE SERVIÇOS ===")

        val services = listOf(
            hashMapOf("name" to "Corte de Cabelo", "duration" to 1, "price" to 25.0),
            hashMapOf("name" to "Barba", "duration" to 1, "price" to 30.0),
            hashMapOf("name" to "Corte e Barba", "duration" to 2, "price" to 75.0),
            hashMapOf("name" to "Sobrancelha", "duration" to 1, "price" to 15.0),
            hashMapOf("name" to "Limpeza de Pele", "duration" to 4, "price" to 45.0)
        )

        for (service in services) {
            try {
                val docRef = servicesCollection.add(service).await()
                println("SUCESSO: Serviço '${service["name"]}' criado com ID: ${docRef.id}")
            } catch (e: Exception) {
                println("ERRO ao criar '${service["name"]}': ${e.message}")
            }
        }

        println("=== SEED FINALIZADO ===")
    }

    @Test
    fun b_deleteAllServices() = runBlocking {
        println("=== LIMPANDO COLEÇÃO DE SERVIÇOS ===")

        try {
            val snapshot = servicesCollection.get().await()
            for (document in snapshot.documents) {
                document.reference.delete().await()
                println("DELETADO: Documento ${document.id}")
            }
            println("=== LIMPEZA CONCLUÍDA ===")
        } catch (e: Exception) {
            println("ERRO ao limpar coleção: ${e.message}")
        }
    }
}
