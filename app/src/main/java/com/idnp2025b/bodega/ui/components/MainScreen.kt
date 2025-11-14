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
import com.idnp2025b.bodega.data.entities.Product
import com.idnp2025b.bodega.ui.components.CustomerItem
import com.idnp2025b.bodega.ui.components.ProductItem
import com.idnp2025b.bodega.viewmodel.BodegaViewModel
import com.idnp2025b.bodega.viewmodel.ClientFormState // <-- IMPORTANTE

@Composable
fun MainScreen(viewModel: BodegaViewModel) {
    val customers by viewModel.allCustomers.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("App Bodega (Room)", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Clientes", fontSize = 22.sp)

        Button(
            onClick = {

                val newId = 0 // El ViewModel se encarga del ID si es 0
                val randomName = "Cliente #${customers.size + 1}"

                // 1. Preparamos el formulario
                viewModel.updateClientFormState(
                    ClientFormState(
                        id = newId,
                        firstName = randomName,
                        lastName = "Test",
                        email = "test${customers.size + 1}@email.com"
                    )
                )

                // 2. Llamamos a la función que SÍ existe
                viewModel.saveClient()
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar")
            Spacer(Modifier.width(8.dp))
            Text("Agregar Cliente")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(customers) { customer ->
                CustomerItem(
                    customer = customer,
                    onDelete = {
                        // --- CORRECCIÓN PARA 'deleteCustomer' ---
                        // La función se llama 'deleteClient' en tu ViewModel
                        viewModel.deleteClient(customer)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Productos", fontSize = 22.sp)

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