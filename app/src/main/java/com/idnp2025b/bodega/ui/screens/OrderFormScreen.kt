package com.idnp2025b.bodega.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idnp2025b.bodega.data.entities.Customer
import com.idnp2025b.bodega.viewmodel.BodegaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFormScreen(
    navController: NavController,
    viewModel: BodegaViewModel,
    orderId: Int
) {
    val formState = viewModel.orderFormState
    val customers by viewModel.allCustomers.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearFormStates()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (orderId == 0) "Nuevo Pedido" else "Editar Pedido",
            style = MaterialTheme.typography.headlineMedium
        )

        // En "Editar", no permitimos cambiar la fecha o el ID.
        if (orderId != 0) {
            Text("Editando Pedido #${formState.id}")
            Text("Fecha: ${formState.date}")
        }

        // Dropdown para Clientes
        CustomerDropdown(
            customers = customers,
            selectedCustomerId = formState.customerId,
            onCustomerSelected = {
                viewModel.updateOrderFormState(formState.copy(customerId = it))
            },
            enabled = (orderId == 0) // Solo se puede elegir cliente al crear
        )

        Button(
            onClick = {
                viewModel.saveOrder()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (orderId == 0) "Crear Pedido" else "Actualizar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDropdown(
    customers: List<Customer>,
    selectedCustomerId: Int,
    onCustomerSelected: (Int) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCustomerName = customers.find { it.CustomerID == selectedCustomerId }
        ?.let { "${it.FirstName} ${it.LastName}" }
        ?: "Seleccionar cliente"

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if(enabled) expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCustomerName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Cliente") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            enabled = enabled,
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            customers.forEach { customer ->
                DropdownMenuItem(
                    text = { Text("${customer.FirstName} ${customer.LastName}") },
                    onClick = {
                        onCustomerSelected(customer.CustomerID)
                        expanded = false
                    }
                )
            }
        }
    }
}