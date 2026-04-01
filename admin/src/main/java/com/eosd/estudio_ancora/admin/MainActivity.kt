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
import com.eosd.estudio_ancora.admin.views.screens.AdminServices
import com.eosd.estudio_ancora.views.theme.Estudio_ancoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Estudio_ancoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AdminServices(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
