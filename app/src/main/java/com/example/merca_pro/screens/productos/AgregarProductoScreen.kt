package com.example.merca_pro.screens.agregarproductos
import androidx.collection.emptyLongSet
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.merca_pro.viewmodel.AgregarProductoViewModel
import com.example.merca_pro.viewmodel.DetalleListaViewModel
import com.example.merca_pro.viewmodel.Producto

//esta pantalla ofrece la posibilidad de agregar productos de Mercadona a una lista de compra
//debe mostrar una serie de campos para filtrar los productos que queremos agregar
@Composable
fun AgregarProductoScreen(idLista: String, navHostController: NavHostController) {

    val viewModel: AgregarProductoViewModel = viewModel()

    val subcategorias = viewModel.subcategorias.value
    val productosPorSubcategoria = viewModel.productos.value

    LaunchedEffect(Unit) {
        try {
            val data = viewModel.obtenerSubcategorias()
            viewModel.subcategorias.value = data
        } catch (e: Exception) {
            // Manejar error aquí (podrías mostrar un Toast o algo similar)
        }
    }

    var subcategoriaSeleccionada by remember { mutableStateOf("") }
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Agregar producto",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown de subcategorías
        Text("Subcategoría:")
        var expandedSub by remember { mutableStateOf(false) }
        Box {
            OutlinedButton(onClick = { expandedSub = true }) {
                Text(if (subcategoriaSeleccionada.isNotEmpty()) subcategoriaSeleccionada else "Seleccione una subcategoría")
            }
            DropdownMenu(expanded = expandedSub, onDismissRequest = { expandedSub = false }) {
                subcategorias.forEach { sub ->
                    DropdownMenuItem(
                        text = { Text(sub.name) },
                        onClick = {
                            viewModel.cargarProductosPorCategoria(sub.id)
                            subcategoriaSeleccionada = sub.name
                            productoSeleccionado = null // Limpiamos producto anterior
                            expandedSub = false
                        })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown de productos según subcategoría
        if (subcategoriaSeleccionada.isNotEmpty()) {
            Text("Producto:")
            var expandedProd by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expandedProd = true }) {
                    Text(productoSeleccionado?.displayName ?: "Seleccione un producto")
                }
                DropdownMenu(expanded = expandedProd, onDismissRequest = { expandedProd = false }) {
                    productosPorSubcategoria?.forEach { producto ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = producto.thumbnail,
                                        contentDescription = "Imagen de ${producto.displayName}",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(producto.displayName)
                                }
                            },
                            onClick = {
                                productoSeleccionado = producto
                                //tomar el producto seleccionado y guardarlo en la lista de compra actual

                                expandedProd = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Aquí llamas a tu ViewModel para guardar el producto
                if(productoSeleccionado != null) {
                    viewModel.agregarProductoALista(idLista, productoSeleccionado!!)
                }
            },
            enabled = subcategoriaSeleccionada.isNotEmpty() && productoSeleccionado != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar")
        }

        val guardadoExitoso = viewModel.guardadoExitoso
        //val idLista = "tu_id_aqui"
        val timestamp = System.currentTimeMillis()

        if (guardadoExitoso != null) {
            AlertDialog(
                onDismissRequest = { viewModel.limpiarEstadoGuardado() },
                confirmButton = {
                    TextButton(onClick = { viewModel.limpiarEstadoGuardado() }) {
                        if (guardadoExitoso) {
                            navHostController.navigate("detalle_lista/$idLista?refresh=$timestamp") {
                                popUpTo("agregarproducto/{idLista}") { inclusive = true }
                            }
                        }
                        Text("OK")
                    }
                },
                title = {
                    Text(if (guardadoExitoso) "¡Guardado exitoso!" else "Error al guardar")
                },
                text = {
                    Text(
                        if (guardadoExitoso)
                            "El producto fue añadido a la lista correctamente."
                        else
                            "Hubo un problema al guardar el producto. Inténtalo nuevamente."
                    )
                }
            )
        }
    }
}