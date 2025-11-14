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
import com.idnp2025b.bodega.data.entities.Order
import com.idnp2025b.bodega.ui.navigation.Screen
import com.idnp2025b.bodega.viewmodel.BodegaViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderListScreen(navController: NavController, viewModel: BodegaViewModel) {
    val orders by viewModel.allOrders.collectAsState()
    val customers by viewModel.allCustomers.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.OrderForm.createRoute(0))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Pedido")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            items(orders) { order ->
                OrderListItem(
                    order = order,
                    // Buscamos el nombre del cliente para mostrarlo
                    customer = customers.find { it.CustomerID == order.CustomerID },
                    onDelete = { viewModel.deleteOrder(order) },
                    onEdit = {
                        navController.navigate(Screen.OrderForm.createRoute(order.OrderID))
                    }
                )
            }
        }
    }
}

@Composable
fun OrderListItem(
    order: Order,
    customer: Customer?,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onEdit() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Pedido #${order.OrderID}", fontWeight = FontWeight.Bold)
                Text(customer?.FirstName ?: "Cliente no encontrado")
                Text(dateFormatter.format(order.OrderDate))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
    }
}