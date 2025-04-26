package com.example.merca_pro.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavScreen(val route: String, val icon: ImageVector, val title: String) {
    //definicion del menu de navegacion
    //nada opcion de menu se corresponde con una pantalla y se tiene que usar su nombre de ruta
    object Home : NavScreen("home", Icons.Default.Home, "Inicio")
    //object Search : NavScreen("listas", Icons.Default.Search, "Listas Compra")
    //object Cart : NavScreen("cart", Icons.Default.ShoppingCart, "Carrito")
    object Profile : NavScreen("profile", Icons.Default.Person, "Perfil")


}
