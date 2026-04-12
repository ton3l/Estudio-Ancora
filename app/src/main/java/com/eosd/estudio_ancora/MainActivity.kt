package com.eosd.estudio_ancora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.eosd.estudio_ancora.services.AuthService
import com.eosd.estudio_ancora.views.components.App
import com.eosd.estudio_ancora.views.theme.Estudio_ancoraTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializa a autenticação anônima em background
        lifecycleScope.launch {
            AuthService.ensureAuthenticated()
        }

        setContent {
            Estudio_ancoraTheme {
                App()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}