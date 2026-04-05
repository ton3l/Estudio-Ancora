package com.eosd.estudio_ancora.models.day

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eosd.estudio_ancora.libs.firestore
import com.eosd.estudio_ancora.models.day.dtos.TimeSlotDocument
import com.eosd.estudio_ancora.models.day.dtos.WeekDayAvailableTimes
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeekDayAvailableTimesModelTest {

    private val testCollection = firestore.collection("week-available-times")
    private val testDocs = listOf(
        WeekDayAvailableTimes("monday-test", true, mapOf("09:00" to true, "10:00" to false)),
        WeekDayAvailableTimes("tuesday-test", false, emptyMap())
    )

    @Before
    fun setup() {
        runBlocking {
            testDocs.forEach { doc ->
                testCollection.document(doc.weekDay).set(doc).await()
            }
        }
    }

    @After
    fun teardown() {
        runBlocking {
            testDocs.forEach { doc ->
                testCollection.document(doc.weekDay).delete().await()
            }
        }
    }

    @Test
    fun getAllWeekDayAvailableTimes_returnsAllDocuments() {
        runBlocking {
            val results = DayModel.getAllWeekDayAvailableTimes()
            
            val testResults = results.filter { it.weekDay.endsWith("-test") }
            assertEquals(2, testResults.size)
            
            val monday = testResults.find { it.weekDay == "monday-test" }
            assertNotNull(monday)
            assertTrue(monday!!.open)
            assertEquals(true, monday.timeSlots["09:00"])
            assertEquals(false, monday.timeSlots["10:00"])

            val tuesday = testResults.find { it.weekDay == "tuesday-test" }
            assertNotNull(tuesday)
            assertTrue(!tuesday!!.open)
        }
    }

    @Test
    fun updateWeekDayAvailableTime_updatesDocumentInFirestore() {
        runBlocking {
            val updatedDoc = WeekDayAvailableTimes(
                weekDay = "tuesday-test",
                open = true,
                timeSlots = mapOf("14:00" to true)
            )

            DayModel.updateWeekDayAvailableTime(updatedDoc)

            val retrieved = testCollection.document("tuesday-test").get().await().toObject(WeekDayAvailableTimes::class.java)
            
            assertNotNull(retrieved)
            assertTrue(retrieved!!.open)
            assertEquals(true, retrieved.timeSlots["14:00"])
        }
    }
}
