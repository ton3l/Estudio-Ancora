package com.eosd.estudio_ancora

import android.app.Application
import com.google.firebase.FirebaseApp

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }
}
