package com.eosd.estudio_ancora

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class FirebaseTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        // Ignora o nome da aplicação padrão e usa a TestApplication
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}
