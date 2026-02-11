package com.eosd.estudio_ancora.ui.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eosd.estudio_ancora.ui.screens.BookingDateSelect
import com.eosd.estudio_ancora.ui.screens.BookingForm
import com.eosd.estudio_ancora.ui.screens.BookingLog

object Routes {
    const val CALENDAR = "calendar"
    const val BOOKING_LOG = "booking_log"
    const val BOOKING_FORM = "booking_form"
}

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            AppHeader()
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
            ) {
                Nav(
                    navController = navController,
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.CALENDAR,
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
            composable(Routes.CALENDAR) {
                BookingDateSelect(
                    paddingValues = innerPadding,
                    onBackPressed = { navController.popBackStack() },
                    navController = navController
                )
            }
            composable(Routes.BOOKING_LOG) {
                BookingLog(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                )
            }
            composable(Routes.BOOKING_FORM) {
                BookingForm(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    navController = navController
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}