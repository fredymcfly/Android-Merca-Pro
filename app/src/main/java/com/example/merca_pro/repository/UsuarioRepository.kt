package com.example.merca_pro.repository

import com.example.merca_pro.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsuarioRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersRef = db.collection("users")

    suspend fun obtenerUsuario(uid: String): Usuario? {
        val snapshot = usersRef.document(uid).get().await()
        return snapshot.toObject(Usuario::class.java)
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        usersRef.document(usuario.userId).set(usuario).await()
    }
}