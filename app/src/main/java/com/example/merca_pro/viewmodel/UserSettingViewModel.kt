package com.example.merca_pro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.merca_pro.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserSettingViewModel : ViewModel() {
//-----------------------Variables y estados------------------------------------------------------------------
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario



//-----------------------Cargar usuario desde Firestore------------------------------------------------------------------
    fun cargarUsuario(uid: String) {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("users") // nombre exacto de tu colección
                    .document(uid)
                    .get()
                    .await()

                val user = snapshot.toObject(Usuario::class.java)
                _usuario.value = user
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
//-------------------------- Actualizar usuario en Firestore----------------------------------------------------------------


    fun actualizarUsuario(usuario: Usuario) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(usuario.userId)
            .set(usuario)
    }

// ----------------- Cambiar contraseña --------------------------------------------------------------
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
//----------------- Eliminar contraseña --------------------------------------------------------------
    fun deleteAccount(onResult: (Boolean) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.delete()
            ?.addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }
}
