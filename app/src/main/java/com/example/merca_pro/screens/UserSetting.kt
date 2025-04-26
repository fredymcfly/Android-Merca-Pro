/*package com.example.merca_pro.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.merca_pro.models.Usuario
import com.example.merca_pro.ui.theme.MercadonaGreen
import com.example.merca_pro.viewmodel.UserSettingViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UserSetting(
    navController: NavHostController,
    viewModel: UserSettingViewModel = viewModel()
) {
    val context = LocalContext.current
    val usuario by viewModel.usuario.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    // Cargar datos del usuario
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        uid?.let { viewModel.cargarUsuario(it) }
    }

    // Cuando el usuario se actualiza, llenar campos
    LaunchedEffect(usuario) {
        val user = usuario
        if (user != null) {
            nombre = user.name
            email = user.email
            fechaNacimiento = user.birthdate
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cabecera con botón de volver
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(end = 8.dp)
            )
        }

        // Avatar
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .padding(16.dp),
            tint = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campos de perfil
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre y Apellidos") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("Correo electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = { fechaNacimiento = it },
                    label = { Text("Fecha de Nacimiento") },
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Actualizar
        Button(
            onClick = {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    val usuarioActualizado = Usuario(
                        userId = uid,
                        name = nombre,
                        email = email,
                        birthdate = fechaNacimiento
                    )
                    viewModel.actualizarUsuario(usuarioActualizado)
                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MercadonaGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("ACTUALIZAR", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Cerrar sesión
        Button(
            onClick = {
                viewModel.logout()
                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("CERRAR SESIÓN")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Eliminar cuenta
        OutlinedButton(
            onClick = {
                viewModel.deleteAccount { exito ->
                    if (exito) {
                        Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Borrar cuenta")
        }
    }
}
*/