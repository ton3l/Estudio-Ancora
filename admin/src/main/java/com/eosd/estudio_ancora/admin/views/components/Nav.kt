package com.eosd.estudio_ancora.admin.views.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun Nav(navController: NavController, modifier: Modifier = Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val options = listOf(
        Triple(AdminRoutes.BOOKING_FLOW, Icons.Default.Event, "Agendar"),
        Triple(AdminRoutes.ADMIN_LOGS, Icons.Default.List, "Histórico"),
        Triple(AdminRoutes.ADMIN_TIMES, Icons.Default.Schedule, "Horários"),
        Triple(AdminRoutes.ADMIN_SERVICES, Icons.Default.ContentCut, "Serviços")
    )

    NavigationBar(modifier = modifier) {
        options.forEach { (route, icon, label) ->
            val selected = currentDestination?.hierarchy?.any { it.route == route } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label
                    )
                },
                label = { Text(label) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavPreview() {
    Nav(rememberNavController(), Modifier.padding(16.dp))
}
