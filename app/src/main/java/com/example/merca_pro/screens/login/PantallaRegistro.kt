package com.example.merca_pro.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.merca_pro.viewmodel.RegistroViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


//onRegistroClick: (String, String,String,String) -> Unit,  // Para registrar usuario Guarda datos y pasa a perfil


@Composable
fun PantallaRegistro(
    viewModel: RegistroViewModel = viewModel(),
    onVolverLoginClick: () -> Unit  // Para volver a la pantalla de login
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))


        // Pestaána de Registro

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onVolverLoginClick) {
                Text("Volver al Login", color = Color.Black)
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009A17))
            ) {
                Text("REGISTRO", color = Color.White)
            }
        }


        // Campo de Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre y Apellidos") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        // Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))


        // 🔹 Campo Fecha de Nacimiento
        OutlinedTextField(
            value = birthdate,
            onValueChange = { birthdate = it },
            label = { Text("Fecha de Nacimiento") },
            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(25.dp))


        OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
                       IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Person else Icons.Filled.Lock,
                    contentDescription = "Mostrar/Ocultar contraseña"
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )


        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                    Icon(
                        imageVector = if (repeatPasswordVisible) Icons.Filled.Person else Icons.Filled.Lock,
                        contentDescription = "Mostrar/Ocultar contraseña"
                    )
                }
            },
            visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(25.dp))


        // 🔹 Mensaje de error si las contraseñas no coinciden o hay errores
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }


        // 🔹 Botón "REGISTRARSE"
        Button(
            onClick = {
                if (!email.contains("@") || !email.contains(".")) {
                    errorMessage = "Correo electrónico no válido"
                } else if (password.length < 6) {
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                } else if (password != repeatPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                } else {
                    errorMessage = "hace click"
                    viewModel.registrarUsuario(name, email, birthdate, password,
                        onSuccess = {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()

                            //redireccion a login
                            onVolverLoginClick() // ← vuelve al login tras registro exitoso

                        },
                        onError = { errorMessage = it })
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("REGISTRARSE", color = Color.White)

        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Botón "Volver al Login"
        TextButton(onClick = onVolverLoginClick) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}
