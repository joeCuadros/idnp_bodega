package com.idnp2025b.bodega.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idnp2025b.bodega.data.entities.Customer
import com.idnp2025b.bodega.ui.navigation.Screen
import com.idnp2025b.bodega.viewmodel.BodegaViewModel

@Composable
fun ClientListScreen(navController: NavController, viewModel: BodegaViewModel) {
    val customers by viewModel.allCustomers.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // ID 0 significa "Crear Nuevo"
                navController.navigate(Screen.ClientForm.createRoute(0))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Cliente")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            items(customers) { client ->
                ClientListItem(
                    customer = client,
                    onDelete = { viewModel.deleteClient(client) },
                    onEdit = {
                        // Ir al formulario con el ID del cliente
                        navController.navigate(Screen.ClientForm.createRoute(client.CustomerID))
                    }
                )
            }
        }
    }
}

@Composable
fun ClientListItem(customer: Customer, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onEdit() }, // Click en la tarjeta para editar
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${customer.FirstName} ${customer.LastName}", fontWeight = FontWeight.Bold)
                Text(customer.Email)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
    }
}