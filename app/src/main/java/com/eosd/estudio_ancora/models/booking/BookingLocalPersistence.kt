package com.eosd.estudio_ancora.models.booking

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.eosd.estudio_ancora.libs.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object BookingLocalPersistence {
    private val BOOKING_IDS_KEY = stringSetPreferencesKey("booking_ids")

    fun getBookingIds(context: Context): Flow<Set<String>> {
        return context.dataStore.data.map { preferences ->
            preferences[BOOKING_IDS_KEY] ?: emptySet()
        }
    }

    suspend fun addBookingId(context: Context, bookingId: String) {
        context.dataStore.edit { preferences ->
            val currentIds = preferences[BOOKING_IDS_KEY] ?: emptySet()
            preferences[BOOKING_IDS_KEY] = currentIds + bookingId
        }
    }

    suspend fun removeBookingId(context: Context, bookingId: String) {
        context.dataStore.edit { preferences ->
            val currentIds = preferences[BOOKING_IDS_KEY] ?: emptySet()
            preferences[BOOKING_IDS_KEY] = currentIds - bookingId
        }
    }
}