package com.example.merca_pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registrarUsuario(
        name: String,
        email: String,
        birthdate: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Crear objeto usuario
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email,
                            "birthdate" to birthdate,
                            "userId" to userId
                        )

                        // Guardar en Firestore
                        db.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                onSuccess() // Usuario registrado y datos guardados
                            }
                            .addOnFailureListener { error ->
                                onError("Error guardando datos: ${error.message}")
                            }
                    } else {
                        onError(task.exception?.message ?: "Error desconocido")
                    }
                }
        }
    }
}

