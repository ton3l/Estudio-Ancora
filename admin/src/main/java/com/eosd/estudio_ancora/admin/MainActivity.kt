package com.eosd.estudio_ancora.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.eosd.estudio_ancora.admin.views.components.AdminApp
import com.eosd.estudio_ancora.services.AuthService
import com.eosd.estudio_ancora.views.theme.Estudio_ancoraTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa a autenticação anônima em background
        lifecycleScope.launch {
            AuthService.ensureAuthenticated()
        }

        setContent {
            Estudio_ancoraTheme {
                AdminApp()
            }
        }
    }
}
