package com.idnp2025b.bodega.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.idnp2025b.bodega.data.entities.Customer
import com.idnp2025b.bodega.ui.components.CustomerItem
import com.idnp2025b.bodega.ui.components.ProductItem
import com.idnp2025b.bodega.viewmodel.BodegaViewModel

@Composable
fun MainScreen(viewModel: BodegaViewModel) {
    // Colectamos los datos (los 'Flows') como 'State' para que Compose reaccione
    val customers by viewModel.allCustomers.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("App Bodega (Room)", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Sección de Clientes (con CRUD) ---
        Text("Clientes", fontSize = 22.sp)

        Button(
            onClick = {
                // Ejemplo de CREATE (CRUD)
                val newId = (customers.maxOfOrNull { it.CustomerID } ?: 0) + 1
                val randomName = "Cliente $newId"
                viewModel.insertCustomer(
                    Customer(
                        CustomerID = newId,
                        FirstName = randomName,
                        LastName = "Test",
                        Email = "test$newId@email.com"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
            Spacer(Modifier.width(8.dp))
            Text("Agregar Cliente")
        }

        // Lista de Clientes (READ)
        // La Carga Inicial de datos aparecerá aquí automáticamente
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(customers) { customer ->
                CustomerItem(
                    customer = customer,
                    onDelete = {
                        // Ejemplo de DELETE (CRUD)
                        viewModel.deleteCustomer(customer)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Sección de Productos (Solo READ) ---
        Text("Productos", fontSize = 22.sp)

        // La Carga Inicial de datos aparecerá aquí
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(products) { product ->
                ProductItem(product = product)
            }
        }
    }
}