package com.example.merca_pro.ui.components

import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.merca_pro.navigation.NavScreen
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        NavScreen.Home,
        NavScreen.Profile
    )

    BottomNavigation(backgroundColor = Color.White) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
