package com.example.merca_pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DetalleListaViewModel : ViewModel() {
    private val auth =
        FirebaseAuth.getInstance() // con esto obtenemos el id del usuario ativo actualmente
    private val db =
        FirebaseFirestore.getInstance()

    fun eliminarListaDeCompra(listaId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                db.collection("users")
                    .document(userId)
                    .collection("shoppingLists")
                    .document(listaId)
                    .delete()
                    .await()

                onSuccess() // Llamamos al callback si se elimina con éxito
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}