package com.eosd.estudio_ancora.admin.views.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eosd.estudio_ancora.admin.views.screens.AdminBookingHistory
import com.eosd.estudio_ancora.admin.views.screens.AdminServices
import com.eosd.estudio_ancora.admin.views.screens.AdminTimes
import com.eosd.estudio_ancora.views.components.AppHeader

object AdminRoutes {
    const val ADMIN_LOGS = "admin_logs"
    const val ADMIN_TIMES = "admin_times"
    const val ADMIN_SERVICES = "admin_services"
}

@Composable
fun AdminApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            AppHeader()
        },
        bottomBar = {
            Nav(navController = navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AdminRoutes.ADMIN_LOGS,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
            }
        ) {
            composable(AdminRoutes.ADMIN_LOGS) {
                AdminBookingHistory(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(AdminRoutes.ADMIN_TIMES) {
                AdminTimes(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(AdminRoutes.ADMIN_SERVICES) {
                AdminServices(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminAppPreview() {
    AdminApp()
}
