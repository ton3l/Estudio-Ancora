package com.eosd.estudio_ancora

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.collections.hashMapOf

@RunWith(AndroidJUnit4::class)
class FirestoreSeederTest {
    @Test
    fun seedDatabase() = runBlocking {
        val db = Firebase.firestore

        println("=== START SEED ===")

        val ids = arrayOf(
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday",
            "saturday",
            "sunday"
        )
        ids.forEach { weekDay ->
            val data = hashMapOf(
                "open" to true,
                "8" to false,
                "9" to false,
                "10" to false,
                "11" to false,
                "12" to false,
                "13" to false,
                "14" to false,
                "15" to false,
                "16" to false,
                "17" to false,
                "18" to false,
                "19" to false,
                "20" to false
            )

            try {
                db.collection("week-available-times")
                    .document(weekDay)
                    .set(data)
                    .await()

                println("SUCESSO: Documento criado com ID: $weekDay")

                val snapshot = db.collection("week-available-times")
                    .document(weekDay)
                    .get()
                    .await()

                println("DADOS LIDOS: ${snapshot.data}")

            } catch (e: Exception) {
                println("ERRO: ${e.message}")
                throw e
            }
        }

        println("=== END SEED ===")
    }
}
