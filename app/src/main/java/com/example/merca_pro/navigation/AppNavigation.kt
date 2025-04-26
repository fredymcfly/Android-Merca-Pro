package com.example.merca_pro.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.merca_pro.screens.agregarproductos.AgregarProductoScreen
import com.example.merca_pro.screens.login.PantallaLogin
import com.example.merca_pro.screens.login.PantallaRegistro
import com.example.merca_pro.screens.home.HomeScreen
import com.example.merca_pro.screens.listas.CrearListaScreen
import com.example.merca_pro.screens.listas.DetalleListaScreen
import com.example.merca_pro.screens.perfil.EditarPerfilScreen
import com.example.merca_pro.screens.perfil.PantallaCambiarContrasena

import com.example.merca_pro.screens.perfil.PerfilScreen


import com.example.merca_pro.ui.components.BottomBar

import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {
    val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val showBottomBar = currentDestination in listOf("home", "profile")

    val user = auth.currentUser

    // ✅ Navegar automáticamente si hay sesión
    LaunchedEffect(user) {
        if (user != null) {
            navHostController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navHostController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navHostController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 🔐 Login y Registro
            composable("login") {
                PantallaLogin(
                    auth = auth,
                    onLoginSuccess = { navHostController.navigate("home") },
                    onRegisterClick = { navHostController.navigate("register") }
                )
            }

            composable("register") {
                PantallaRegistro(
                    onVolverLoginClick = { navHostController.navigate("login") }
                )
            }

            //---------------- Pantalla principal-------------------
            composable("home") {
                HomeScreen(navHostController)
            }

            // ----------- Perfil del usuario -----------------------
            composable("profile") {
                PerfilScreen(
                    navController = navHostController,
                    onEditarPerfil = { navHostController.navigate("editar_perfil") },
                    onCambiarContrasena = { navHostController.navigate("cambiar_contrasena") },
                    onBorrarCuenta = { /* lógica borrar */ },
                    onCerrarSesion = {
                        FirebaseAuth.getInstance().signOut()
                        navHostController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            // ----------------- Pantalla de edición de perfil -----------------
                // ✏️ Editar perfil (debes tener EditarPerfilScreen creada)

            composable(
                route = "editar_perfil?nombre={nombre}&email={email}&birthdate={birthdate}",
                arguments = listOf(
                    navArgument("nombre") { defaultValue = "" },
                    navArgument("email") { defaultValue = "" },
                    navArgument("birthdate") { defaultValue = "" }
                )
            ) { backStackEntry ->
                val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                val email = backStackEntry.arguments?.getString("email") ?: ""
                val birthdate = backStackEntry.arguments?.getString("birthdate") ?: ""

                EditarPerfilScreen(
                    navController = navHostController,
                    nombreInicial = nombre,
                    emailInicial = email,
                    birthdateInicial = birthdate
                ) { nuevoNombre, nuevoEmail, nuevaFecha ->
                    navHostController.popBackStack() // Volver al perfil
                }
            }
//-----------------------------Cambiar contraseña-------------------
            composable("cambiar_contrasena") {
                // Pantalla para cambiar la contraseña
                PantallaCambiarContrasena(navController = navHostController)
            }
//----------------------------Crear lista-------------------------------------

            composable("crearlista") {
                CrearListaScreen(navController = navHostController)
            }

//----------------------------Detalle lista-------------------------------------
            composable("detalle_lista/{listaId}") { backStackEntry ->
                val listaId = backStackEntry.arguments?.getString("listaId") ?: ""
                DetalleListaScreen(listaId,navHostController)
            }

//----------------------------Agregar producto-------------------------------------
            composable("agregarproducto/{listaId}"){ backStackEntry ->
                val listaId = backStackEntry.arguments?.getString("listaId") ?: ""
                AgregarProductoScreen(listaId,navHostController)
            }

        }
    }
}
