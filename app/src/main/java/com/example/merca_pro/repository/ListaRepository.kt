package com.example.merca_pro.repository



import android.adservices.adid.AdId
import com.example.merca_pro.models.ListaCompra
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class ListaRepository {
    private val db = FirebaseFirestore.getInstance()
    private val listasRef = db.collection("listas")

    // Función para Crear una nueva lista de compra

    suspend fun crearLista (lista : ListaCompra){
        listasRef.add(lista).await()
    }

    suspend fun leerListasPorUsuario(usuarioId: String):List<ListaCompra>{
        val snapshot = listasRef.whereEqualTo("usuarioId", usuarioId).get().await()
        return snapshot.toObjects(ListaCompra::class.java)   }


    // Función para actualizar un Usuario

    suspend fun actualizarUsuario(usuario: ListaCompra) {
        listasRef.document(usuario.userId).set(usuario).await()
    }

// Función para eliminar un Usuario

    suspend fun eliminarUsuario(uid: String) {
        listasRef.document(uid).delete().await()
    }
}