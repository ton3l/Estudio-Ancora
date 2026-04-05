package com.eosd.estudio_ancora

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.views.utils.toHHmm
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class FirestoreSeederTest {
    lateinit var db: FirebaseFirestore
    val ids = arrayOf(
        "monday",
        "tuesday",
        "wednesday",
        "thursday",
        "friday",
        "saturday",
        "sunday"
    )

    @Before
    fun setUp() {
        db = FirebaseFirestore.getInstance()
    }

    @Test
    fun seedDatabase() = runBlocking {
        println("=== START SEED ===")

        val timeSlots = (6..23).associate { hour ->
            LocalTime.of(hour, 0).toHHmm() to (hour >= 13)
        }

        ids.forEach { weekDay ->
            val data = hashMapOf(
                "open" to true,
                "timeSlots" to timeSlots
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

//    @Test
//    fun cleanWeekAvailableTimes() = runBlocking {
//        ids.forEach { weekDay ->
//            db.collection("week-available-times")
//                .document(weekDay)
//                .delete()
//                .await()
//        }
//    }
}
