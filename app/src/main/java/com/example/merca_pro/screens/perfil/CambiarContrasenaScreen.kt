// PantallaCambiarContrasena.kt
package com.example.merca_pro.screens.perfil

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCambiarContrasena(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cambiar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Campo nueva contraseña
            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                label = { Text("Nueva Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.Lock else Icons.Filled.Lock
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(icon, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo confirmar contraseña
            OutlinedTextField(
                value = confirmarContrasena,
                onValueChange = { confirmarContrasena = it },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val icon = if (confirmPasswordVisible) Icons.Filled.Lock else Icons.Filled.Lock
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(icon, contentDescription = if (confirmPasswordVisible) "Ocultar" else "Mostrar")
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón actualizar
            Button(
                onClick = {
                    when {
                        nuevaContrasena.isBlank() || confirmarContrasena.isBlank() -> {
                            mensaje = "Los campos no pueden estar vacíos."
                        }

                        nuevaContrasena != confirmarContrasena -> {
                            mensaje = "Las contraseñas no coinciden."
                        }

                        nuevaContrasena.length < 6 -> {
                            mensaje = "La contraseña debe tener al menos 6 caracteres."
                        }

                        else -> {
                            auth.currentUser?.updatePassword(nuevaContrasena)
                                ?.addOnCompleteListener { task ->
                                    mensaje = if (task.isSuccessful) {
                                        navController.popBackStack() // Vuelve a la pantalla anterior
                                        "Contraseña actualizada correctamente."
                                    } else {
                                        "Error al actualizar: ${task.exception?.message}"
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ACTUALIZAR CONTRASEÑA", fontSize = 16.sp)
            }

            // Mensaje de estado
            mensaje?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = if (it.contains("correctamente")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
