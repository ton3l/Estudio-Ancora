package com.eosd.estudio_ancora

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookingSeederTest {

    private lateinit var db: FirebaseFirestore

    @Before
    fun setUp() {
        db = FirebaseFirestore.getInstance()
    }

    @Test
    fun deleteAllBookings() = runBlocking {
        println("=== INICIANDO LIMPEZA DA COLEÇÃO DE AGENDAMENTOS ===")
        val bookingsCollection = db.collection("bookings")

        try {
            val snapshot = bookingsCollection.get().await()
            val total = snapshot.size()
            println("Encontrados $total agendamentos para remover.")

            for (document in snapshot.documents) {
                document.reference.delete().await()
                println("DELETADO: Agendamento ${document.id}")
            }
            println("=== LIMPEZA DE AGENDAMENTOS CONCLUÍDA ===")
        } catch (e: Exception) {
            println("ERRO ao limpar coleção de agendamentos: ${e.message}")
            throw e
        }
    }
}
