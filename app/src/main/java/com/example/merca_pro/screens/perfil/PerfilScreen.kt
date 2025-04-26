package com.example.merca_pro.screens.perfil

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
@Composable
fun PerfilScreen(
    navController: NavController,
    onEditarPerfil: () -> Unit,
    onCambiarContrasena: () -> Unit,
    onBorrarCuenta: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var mostrarDialogoBorrarCuenta by remember { mutableStateOf(false) }



    if (mostrarDialogoBorrarCuenta) {
        DialogoBorrarCuenta(
            onDismiss = { mostrarDialogoBorrarCuenta = false },
            onConfirmar = { password ->
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(context, "Cuenta eliminada correctamente", Toast.LENGTH_LONG).show()


                if (user?.email != null) {
                    val credential = EmailAuthProvider.getCredential(user.email!!, password)

                    user.reauthenticate(credential)
                        .addOnSuccessListener {
                            user.delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Cuenta eliminada correctamente", Toast.LENGTH_LONG).show()
                                    mostrarDialogoBorrarCuenta = false
                                    onCerrarSesion() // Aquí puedes volver a la pantalla de login
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error al borrar cuenta", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        )
    }



    if (mostrarDialogo) {
        MostrarDialogoReautenticacion(
            onDismiss = { mostrarDialogo = false },
            onPasswordConfirmed = { passwordActual ->
                val user = FirebaseAuth.getInstance().currentUser
                val email = user?.email

                if (email != null) {
                    val credential = EmailAuthProvider.getCredential(email, passwordActual)
                    user.reauthenticate(credential)
                        .addOnSuccessListener {
                            mostrarDialogo = false
                            navController.navigate("cambiar_contrasena")
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        )
    }


    LaunchedEffect(userId) {
        userId?.let {
            try {
                val doc = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(it)
                    .get()
                    .await()

                nombre = doc.getString("name") ?: ""
                email = doc.getString("email") ?: ""
                fechaNacimiento = doc.getString("birthdate") ?: ""
                cargando = false
            } catch (e: Exception) {
                Log.e("PerfilScreen", "Error al cargar perfil", e)
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    ),
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


            // 🧾 Tarjeta con el nombre
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // 📧 Tarjeta con email
            InfoCard(
                icon = Icons.Default.Email,
                title = "Correo electronico",
                content = email
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🗓️ Tarjeta con fecha
            InfoCard(
                icon = Icons.Default.DateRange,
                title = "Fecha de inscripción",
                content = fechaNacimiento
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ⚙️ Acciones
            BotonAccion(Icons.Default.Edit, "Editar Perfil", onEditarPerfil)
            BotonAccion(Icons.Default.Lock, "Cambiar Contraseña", onCambiarContrasena)
            BotonAccion(Icons.Default.Delete, "Borrar Cuenta") { mostrarDialogoBorrarCuenta = true }
            BotonAccion(Icons.Default.ExitToApp, "Cerrar Sesión", onCerrarSesion)
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun BotonAccion(icon: ImageVector, texto: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(texto, fontWeight = FontWeight.Bold)
    }
}
//------------------funión para mostrar el dialogo de borarr-------------------
@Composable
fun DialogoBorrarCuenta(
    onDismiss: () -> Unit,
    onConfirmar: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Borrar Cuenta") },
        text = {
            Column {
                Text("Introduce tu contraseña para borrar la cuenta:")
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmar(password)
            }) {
                Text("ACEPTAR")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR")
            }
        }
    )
}



//------------------funición para mostrar el dialogo de reautenticación-------------------
@Composable
fun MostrarDialogoReautenticacion(
    onDismiss: () -> Unit,
    onPasswordConfirmed: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar Contraseña") },
        text = {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña actual") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onPasswordConfirmed(password)
            }) {
                Text("ACEPTAR", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR")
            }
        }
    )
}
//---funcioón perfil header-------------------------------------------------------------
@Composable
fun PerfilHeader(nombre: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }

        Card(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(240.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}