package com.example.merca_pro.screens.listas
import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.merca_pro.models.ListaCompra
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import androidx.compose.material3.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CrearListaScreen(navController: NavController){

    val auth = FirebaseAuth.getInstance()
    val db =  FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid?:  return

    var nombreLista by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Nueva Lista de Compra 🛒",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )



        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombreLista,
            onValueChange = { nombreLista = it },
            label = { Text("Nombre de la lista") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (nombreLista.isNotBlank()) {
                    val listaRef = db.collection("users")
                        .document(userId)
                        .collection("shoppingLists")
                        .document()

                    val lista = ListaCompra(
                        id = listaRef.id,
                        nombre = nombreLista,
                        fecha = Date().toString(),
                        userId = userId
                    )

                    listaRef.set(lista)
                        .addOnSuccessListener {
                            mensaje = "Lista creada correctamente "
                            navController.popBackStack() // Vuelve atrás
                        }
                        .addOnFailureListener {
                            mensaje = "Error al crear lista ❌"
                        }
                } else {
                    mensaje = "El nombre no puede estar vacío"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear lista")
        }

        mensaje?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = if (it.contains("✅")) Color.Green else Color.Red)
        }
    }
}