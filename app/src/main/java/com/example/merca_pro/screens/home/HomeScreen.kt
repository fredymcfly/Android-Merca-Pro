package com.example.merca_pro.screens.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.merca_pro.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.merca_pro.viewmodel.ListaViewModel



@Composable

fun HomeScreen (navHostController: NavHostController) {

    val viewModel: ListaViewModel = viewModel()
    val listasCompra by viewModel.listas.collectAsState()
    // Estado para saber qué pestaña está seleccionada (0 = Productos, 1 = Folletos)
    var selectedTabIndex by remember { mutableStateOf(0) }
    // Lista de títulos para las pestañas
    val tabs = listOf("LISTAS")
    // Lista de productos simulada
    LaunchedEffect(Unit) {
        viewModel.cargarListasDelUsuario()
    }

    Image(
        painter = painterResource(id = R.drawable.pexels_photo_27759067),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize() // Ajusta la opacidad según sea necesario

    )


    // Texto de bienvenida personalizado, con estilo moderno
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("¡Bienvenido A Merca-Pro Alfred Mba",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para agregar listas de compra (acción futura)

        Button(
            onClick = {
                navHostController.navigate("crearlista") // 🔁 Usa la misma ruta que tienes registrada
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Lista de Compra")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

       Spacer(modifier = Modifier.height(12.dp))

        if (selectedTabIndex == 0) {
            LazyColumn {
                items(listasCompra) { lista ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable{
                                navHostController.navigate("detalle_lista/${lista.id}")
                            },

                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {

                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = lista.fecha, fontWeight = FontWeight.Bold)
                                Text(text = lista.nombre)
                            }
                        }
                    }
                }
            }
        }

    }
}

data class Producto(
    val marca: String,
    val nombre: String,
    val imageRes: Int
)

    // Aquí puedes agregar el contenido de la pantalla de inicio
    // Por ejemplo, una lista de productos o cualquier otro contenido relevante
