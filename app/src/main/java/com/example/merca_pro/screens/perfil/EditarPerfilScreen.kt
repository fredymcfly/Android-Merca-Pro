// EditarPerfilScreen.kt
package com.example.merca_pro.screens.perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.merca_pro.R
import kotlinx.coroutines.launch

@Composable
fun EditarPerfilScreen(
    navController: NavController,
    nombreInicial: String,
    emailInicial: String,
    birthdateInicial: String,
    onActualizarClick: (String, String, String) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial) }
    var email by remember { mutableStateOf(emailInicial) }
    var birthdate by remember { mutableStateOf(birthdateInicial) }
    val scope = rememberCoroutineScope()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Editar Perfil", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = birthdate,
            onValueChange = { birthdate = it },
            label = { Text("Fecha de Nacimiento") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                uid?.let {
                    scope.launch {
                        val user = mapOf(
                            "name" to nombre,
                            "email" to email,
                            "birthdate" to birthdate,
                            "userId" to uid
                        )
                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                onActualizarClick(nombre, email, birthdate)
                            }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ACTUALIZAR")
        }


    }
}
