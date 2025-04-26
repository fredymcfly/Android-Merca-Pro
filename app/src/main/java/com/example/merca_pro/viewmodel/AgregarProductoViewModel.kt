package com.example.merca_pro.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class AgregarProductoViewModel : ViewModel() {

    private val client = OkHttpClient()

    val subcategorias = mutableStateOf<List<Subcategoria>>(emptyList())
    val productos = mutableStateOf<List<Producto>>(emptyList())

    suspend fun obtenerSubcategorias(): List<Subcategoria> {
        return withContext(Dispatchers.IO) {
            val url = "https://tienda.mercadona.es/api/categories/"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val lista = mutableListOf<Subcategoria>()

            if (!response.isSuccessful || responseBody == null) {
                throw Exception("Error al obtener datos: ${response.message}")
            }

            val json = JSONObject(responseBody)
            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val categoria = results.getJSONObject(i)
                val subcategorias = categoria.getJSONArray("categories")
                for (j in 0 until subcategorias.length()) {
                    val subcat = subcategorias.getJSONObject(j)
                    lista.add(Subcategoria(subcat.getInt("id"), subcat.getString("name")))
                }
            }
            lista
        }
    }

    fun cargarProductosPorCategoria(categoriaId: Int) {
        viewModelScope.launch {
            val lista = mutableListOf<Producto>()
            val url = "https://tienda.mercadona.es/api/categories/$categoriaId"
            val request = Request.Builder().url(url).build()

            try {

                // Aquí hacemos la llamada a la API
                //inicialmente esta peticion la haciamos en el hilo principal
                //pero tras revisar un error al ejecutar la peticion habia que hacerlo
                //usando el hilo de red
                //val response = client.newCall(request).execute()

                // Esto lo hacemos en el hilo de red (no en el main)
                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null) {
                    throw Exception("Error al obtener productos: ${response.message}")
                }

                val json = JSONObject(responseBody)
                val subcategorias = json.getJSONArray("categories")

                for (i in 0 until subcategorias.length()) {
                    val subcat = subcategorias.getJSONObject(i)
                    val productosArray = subcat.getJSONArray("products")

                    for (j in 0 until productosArray.length()) {
                        val prod = productosArray.getJSONObject(j)
                        val priceInfo = prod.getJSONObject("price_instructions")

                        lista.add(
                            Producto(
                                id = prod.getString("id"),
                                packaging = prod.getString("packaging"),
                                thumbnail = prod.getString("thumbnail"),
                                displayName = prod.getString("display_name"),
                                unitPrice = priceInfo.getString("unit_price")
                            )
                        )
                    }
                }

                productos.value = lista

            } catch (e: Exception) {
                // Aquí podrías manejar errores con un estado adicional (por ejemplo: mensaje de error)
                e.printStackTrace()
            }
        }
    }


    var guardadoExitoso by mutableStateOf<Boolean?>(null)
        private set

    fun agregarProductoALista(idLista: String, producto: Producto) {
        // Aquí haces la lógica de guardar el producto en Firestore bajo la lista correspondiente
        val db = FirebaseFirestore.getInstance()
        val auth =
            FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("shoppingLists")
            .document(idLista)
            .collection("productos")
            .document(producto.id) // usamos el ID del producto como ID del documento
            .set(producto)
            .addOnSuccessListener {
                guardadoExitoso = true
            }
            .addOnFailureListener { e ->
                guardadoExitoso = false
            }

    }
    fun limpiarEstadoGuardado() {
        guardadoExitoso = null
    }

}

data class Producto(
    val id: String,
    val packaging: String,
    val thumbnail: String,
    val displayName: String,
    val unitPrice: String
)

data class Subcategoria(
    val id: Int,
    val name: String
)