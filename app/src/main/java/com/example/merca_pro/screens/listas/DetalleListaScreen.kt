package com.example.merca_pro.screens.listas
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.merca_pro.viewmodel.DetalleListaViewModel
import com.example.merca_pro.viewmodel.ListaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun DetalleListaScreen(listaId: String,navHostController: NavHostController) {

    val viewModel: DetalleListaViewModel = viewModel()

    // Estado para mostrar el diálogo de confirmación
    var mostrarDialogoConfirmacion by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid ?: return

    var productos by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    // Cargar productos al inicio
    LaunchedEffect(listaId) {
        try {
            val snapshot = db.collection("users")
                .document(userId)
                .collection("shoppingLists")
                .document(listaId)
                .collection("productos")
                .get()
                .await()

            productos = snapshot.documents.map { it.data ?: emptyMap() }
        } catch (e: Exception) {
            e.printStackTrace()
            "No se pudieron cargar los productos"
        } finally {
            cargando = false
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Button(
            onClick = {
                mostrarDialogoConfirmacion = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Borrar Lista de Compra")
        }

        Spacer(modifier = Modifier.height(12.dp))


        Button(
            onClick = {

                navHostController.navigate("agregarproducto/${listaId}")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Producto")
        }

        Text("Productos en tu lista", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (cargando) {
            CircularProgressIndicator()
        } else if (productos.isEmpty()) {
            Text("Esta lista aún no tiene productos.")
        } else {
            productos.forEach { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen pequeña a la izquierda
                        AsyncImage(
                            model = producto["thumbnail"]?.toString(),
                            contentDescription = "Imagen del producto",
                            modifier = Modifier
                                .size(60.dp) // más pequeña que antes
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Textos a la derecha de la imagen
                        Column {
                            Text(text = producto["displayName"]?.toString() ?: "Sin nombre")
                            Text(text = "Precio: ${producto["unitPrice"] ?: "?"} €")
                        }
                    }
                }
            }
        }
    }


    if (mostrarDialogoConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoConfirmacion = false
            },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que quieres borrar esta lista de compra?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoConfirmacion = false
                        viewModel.eliminarListaDeCompra(
                            listaId = listaId,
                            onSuccess = {
                                navHostController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onError = {
                                Log.e("Firestore", "Error al eliminar la lista", it)
                            }
                        )
                    }
                ) {
                    Text("Sí, borrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoConfirmacion = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

