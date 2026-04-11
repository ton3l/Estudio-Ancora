package com.eosd.estudio_ancora

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.utils.Seeder
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@Seeder
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ServiceSeeder {

    private lateinit var db: FirebaseFirestore
    private lateinit var servicesCollection: CollectionReference

    @Before
    fun setUp() {
        db = FirebaseFirestore.getInstance()
        servicesCollection = db.collection("services")
    }

    @Test
    fun a_seedServices() = runBlocking {
        println("=== INICIANDO SEED DE SERVIÇOS ===")

        val services = listOf(
            hashMapOf("name" to "Corte de Cabelo", "duration" to 1, "price" to 25.0),
            hashMapOf("name" to "Barba", "duration" to 1, "price" to 20.0),
            hashMapOf("name" to "Corte e Barba", "duration" to 2, "price" to 35.0),
            hashMapOf("name" to "Sobrancelha", "duration" to 1, "price" to 15.0),
            hashMapOf("name" to "Corte, Barba e Sobrancelha", "duration" to 2, "price" to 45.0),
            hashMapOf("name" to "Selagem", "duration" to 3, "price" to 70.0)
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
