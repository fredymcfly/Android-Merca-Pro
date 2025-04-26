package com.example.merca_pro.screens.login



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.merca_pro.R
import com.example.merca_pro.ui.theme.MercadonaGreen
import com.example.merca_pro.viewmodel.LoginState
import com.example.merca_pro.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth



@Composable
fun PantallaLogin(
    auth: FirebaseAuth,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    //onLoginClick: (String, String) -> Unit,
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        // 🔹 Botones alineados a izquierda y derecha correctamente
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.login(email, password)  },
                colors = ButtonDefaults.buttonColors(containerColor = MercadonaGreen)
            ) {
                Text("INICIAR SESIÓN", color = Color.White)
            }

            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(containerColor = MercadonaGreen)
            ) {
                Text("REGISTRO", color = Color.White)
            }
        }

        Spacer(Modifier.height(24.dp))

        // 🔹 LOGO
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la app",
           // modifier = Modifier.clip(CircleShape)
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Campo de Correo Electrónico
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico", color = MercadonaGreen) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MercadonaGreen,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = MercadonaGreen
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Campo de Contraseña
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = MercadonaGreen) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MercadonaGreen,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = MercadonaGreen
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Botón "ENTRAR"
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MercadonaGreen)
        ) {
            Text("ENTRAR", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Enlace "¿No tienes cuenta?"
        TextButton(onClick = onRegisterClick) {
            Text("¿No tienes cuenta? Regístrate Aquí ", color = MercadonaGreen)
        }

        // 🔹 Manejando estados del ViewModel
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            is LoginState.Error -> {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            is LoginState.Success -> {
                LaunchedEffect(Unit) { onLoginSuccess() }
            }
            else -> {}
        }
    }
}

