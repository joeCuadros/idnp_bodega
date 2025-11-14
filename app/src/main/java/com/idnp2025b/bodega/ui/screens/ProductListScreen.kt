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
import com.idnp2025b.bodega.data.entities.Product
import com.idnp2025b.bodega.ui.navigation.Screen
import com.idnp2025b.bodega.viewmodel.BodegaViewModel

@Composable
fun ProductListScreen(navController: NavController, viewModel: BodegaViewModel) {
    val products by viewModel.allProducts.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.ProductForm.createRoute(0))
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Producto")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            items(products) { product ->
                ProductListItem(
                    product = product,
                    onDelete = { viewModel.deleteProduct(product) },
                    onEdit = {
                        navController.navigate(Screen.ProductForm.createRoute(product.ProductID))
                    }
                )
            }
        }
    }
}

@Composable
fun ProductListItem(product: Product, onDelete: () -> Unit, onEdit: () -> Unit) {
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
                Text(product.ProductName, fontWeight = FontWeight.Bold)
                Text("S/ ${"%.2f".format(product.Price)}")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar")
            }
        }
    }
}