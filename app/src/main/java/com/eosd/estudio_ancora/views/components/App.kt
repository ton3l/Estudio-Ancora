package com.eosd.estudio_ancora.views.components

import android.annotation.SuppressLint
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.eosd.estudio_ancora.views.screens.BookingDateSelect
import com.eosd.estudio_ancora.views.screens.BookingForm
import com.eosd.estudio_ancora.views.screens.BookingLog
import com.eosd.estudio_ancora.views.viewModels.BookingViewModel

object Routes {
    const val BOOKING_DATE_SELECT = "calendar"
    const val BOOKING_LOG = "booking_log"
    const val BOOKING_FORM = "booking_form"
    const val BOOKING_FLOW = "booking_flow"
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
                horizontalArrangement = Arrangement.End
            ) {
                Nav(
                    navController = navController,
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.BOOKING_FLOW,
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
            navigation(
                startDestination = Routes.BOOKING_DATE_SELECT,
                route = Routes.BOOKING_FLOW
            ) {
                composable(Routes.BOOKING_DATE_SELECT) {
                    val sharedViewModel = getSharedViewModel(navController, Routes.BOOKING_FLOW)
                    BookingDateSelect(
                        paddingValues = innerPadding,
                        viewModel = sharedViewModel,
                        onBackPressed = { navController.popBackStack() },
                        onDateTimeSelected = { navController.navigate(Routes.BOOKING_FORM) }
                    )
                }
                composable(Routes.BOOKING_FORM) {
                    val sharedViewModel = getSharedViewModel(navController, Routes.BOOKING_FLOW)
                    BookingForm(
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        viewModel = sharedViewModel,
                        onServiceBooked = { navController.navigate(Routes.BOOKING_LOG) }
                    )
                }
            }
            composable(Routes.BOOKING_LOG) {
                BookingLog(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun getSharedViewModel(navController: NavController, route: String): BookingViewModel {
    val backStackEntry = remember(route) {
        navController.getBackStackEntry(route)
    }
    return viewModel(backStackEntry)
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}