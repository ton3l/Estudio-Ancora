package com.eosd.estudio_ancora.views.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        Routes.BOOKING_FLOW to Icons.Default.CalendarMonth,
        Routes.BOOKING_LOG to Icons.Default.Menu
    )

    Surface (
        shadowElevation = 2.dp,
        modifier = modifier,
        shape = CircleShape
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .height(56.dp)
        ) {
            options.forEachIndexed { index, (route, icon) ->
                val selected = currentDestination?.hierarchy?.any { it.route == route } == true
                
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size,
                    ),
                    onClick = {
                        if (!selected) {
                            navController.navigate(route) {
                                // Volta para o destino inicial do grafo para evitar acúmulo de telas
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Evita múltiplas instâncias da mesma tela
                                launchSingleTop = true
                                // Restaura o estado anterior (ex: se estava no formulário, volta para o formulário)
                                restoreState = true
                            }
                        }
                    },
                    selected = selected,
                    label = {
                        Icon(
                            imageVector = icon,
                            contentDescription = route
                        )
                    },
                    icon = {},
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        activeContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        activeBorderColor = Color.Transparent,
                        inactiveBorderColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavPreview() {
    Nav(rememberNavController(), Modifier.padding(16.dp))
}