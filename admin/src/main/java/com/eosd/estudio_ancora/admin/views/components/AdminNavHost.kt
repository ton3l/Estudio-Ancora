package com.eosd.estudio_ancora.admin.views.components

import android.annotation.SuppressLint
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.eosd.estudio_ancora.admin.views.screens.AdminBookingHistory
import com.eosd.estudio_ancora.admin.views.screens.AdminServices
import com.eosd.estudio_ancora.admin.views.screens.AdminTimes
import com.eosd.estudio_ancora.views.components.AppHeader
import com.eosd.estudio_ancora.views.screens.BookingDateSelect
import com.eosd.estudio_ancora.views.screens.BookingForm
import com.eosd.estudio_ancora.views.viewModels.BookingViewModel

object AdminRoutes {
    const val ADMIN_LOGS = "admin_logs"
    const val ADMIN_TIMES = "admin_times"
    const val ADMIN_SERVICES = "admin_services"
    const val BOOKING_FLOW = "booking_flow"
    const val BOOKING_DATE_SELECT = "calendar"
    const val BOOKING_FORM = "booking_form"
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
            startDestination = AdminRoutes.BOOKING_FLOW,
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
            navigation(
                startDestination = AdminRoutes.BOOKING_DATE_SELECT,
                route = AdminRoutes.BOOKING_FLOW
            ) {
                composable(AdminRoutes.BOOKING_DATE_SELECT) {
                    val sharedViewModel = getSharedViewModel(navController, AdminRoutes.BOOKING_FLOW)
                    BookingDateSelect(
                        paddingValues = innerPadding,
                        viewModel = sharedViewModel,
                        onBackPressed = { navController.popBackStack() },
                        onDateTimeSelected = { navController.navigate(AdminRoutes.BOOKING_FORM) },
                        availableTimesHandler = { state, onSelected -> 
                            AdminAvailableTimesHandler(state, onSelected) 
                        }
                    )
                }
                composable(AdminRoutes.BOOKING_FORM) {
                    val sharedViewModel = getSharedViewModel(navController, AdminRoutes.BOOKING_FLOW)
                    BookingForm(
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding())
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        viewModel = sharedViewModel,
                        onSubmit = {
                            sharedViewModel.createBooking() {
                                navController.navigate(AdminRoutes.ADMIN_LOGS) {
                                    popUpTo(AdminRoutes.BOOKING_FLOW) { inclusive = true }
                                }
                            }
                        }
                    )
                }
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
fun AdminAppPreview() {
    AdminApp()
}
