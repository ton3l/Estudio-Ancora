package com.eosd.estudio_ancora.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eosd.estudio_ancora.ui.screens.SchedulingForm

@Composable
fun App() {
    // 1. Crie o NavController
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            // Passe o navController para o Header para que ele possa navegar
            AppHeader(modifier = Modifier)
        },
    ) { innerPadding ->
        // 2. Crie o NavHost, que funcionará como o "corpo" da sua tela
        NavHost(
            navController = navController,
            startDestination = "form", // Rota inicial
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .padding(vertical = 16.dp),
        ) {
            composable("form") {
                SchedulingForm(modifier = Modifier, navController = navController)
            }
        }
    }
}