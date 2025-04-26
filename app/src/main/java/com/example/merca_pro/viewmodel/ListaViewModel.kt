package com.example.merca_pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.merca_pro.models.ListaCompra
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ListaViewModel : ViewModel() {
// Logica para manejar las listas de compras desde firebase

    private val _listas =
        MutableStateFlow<List<ListaCompra>>(emptyList()) // variable observable cualquier cambio se vera en la pantalla
    private val auth =
        FirebaseAuth.getInstance() // con esto obtenemos el id del usuario ativo actualmente
    private val db =
        FirebaseFirestore.getInstance() // instancia de firebase para acceder a la base de datos
    val listas: StateFlow<List<ListaCompra>> = _listas

    // este init se ejecuta automaticamente al crear la ViewModel
    init {
        cargarListasDelUsuario()
    }

    fun cargarListasDelUsuario() {
        val userId = auth.currentUser?.uid
            ?: return // si no hay usuario activo no se puede cargar la lista se para


        viewModelScope.launch {


                viewModelScope.launch {
                    try {
                        val snapshot = db.collection("users")
                            .document(userId)
                            .collection("shoppingLists")
                            .get()
                            .await() // 👈 esperamos el resultado

                        val listas = snapshot.map { doc ->
                            doc.toObject(ListaCompra::class.java)
                        }

                        _listas.value = listas
                    } catch (e: Exception) {
                        // Manejo de errores
                        e.printStackTrace()
                    }
                }
            }
        }
    }
