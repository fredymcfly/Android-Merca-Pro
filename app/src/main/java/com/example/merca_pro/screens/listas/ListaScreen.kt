package com.example.merca_pro.screens.listas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.merca_pro.viewmodel.ListaViewModel

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items


@Composable
fun ListasScreen(viewModel: ListaViewModel = viewModel()) {
    val listas by viewModel.listas.collectAsState()

    Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
        Text("Tus listas de compra", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        if (listas.isEmpty()) {
            Text("No tienes listas aún.")
        } else {
            LazyColumn {
                items(listas) { lista ->
                    Card(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
                            Text(text = lista.nombre, fontWeight = FontWeight.Bold)
                            Text(text = "Creada: ${lista.fecha}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
