package com.example.merca_pro.screens.perfil
/*
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun EditarPerfilScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    var nombre by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        userId?.let {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(it)
                    .get()
                    .await()

                nombre = doc.getString("name") ?: ""
                fechaNacimiento = doc.getString("birthdate") ?: ""
                cargando = false
            } catch (e: Exception) {
                Log.e("EditarPerfilScreen", "Error al cargar datos", e)
                cargando = false
            }
        }
    }

    if (cargando) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔙 Botón volver
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    text = "Editar Perfil",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // 👤 Icono decorativo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(color = Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ✏️ Campo nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 📅 Fecha
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha de nacimiento") },
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ✅ Botón Guardar
            Button(
                onClick = {
                    userId?.let {
                        val userMap = mapOf(
                            "name" to nombre,
                            "birthdate" to fechaNacimiento
                        )

                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(it)
                            .update(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GUARDAR CAMBIOS", fontWeight = FontWeight.Bold)






   NO FUNCIONA  NO FUNNCIONA







 */

/*/