package com.idnp2025b.bodega.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idnp2025b.bodega.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate(Screen.ClientList.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar Clientes")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(Screen.ProductList.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar Productos")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(Screen.OrderList.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Gestionar Pedidos")
        }
    }
}