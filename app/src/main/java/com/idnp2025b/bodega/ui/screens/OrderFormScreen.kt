package com.idnp2025b.bodega.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idnp2025b.bodega.data.entities.Customer
import com.idnp2025b.bodega.data.entities.OrderDetailWithProduct
import com.idnp2025b.bodega.data.entities.Product
import com.idnp2025b.bodega.viewmodel.BodegaViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFormScreen(
    navController: NavController,
    viewModel: BodegaViewModel,
    orderId: Int
) {
    val formState = viewModel.orderFormState
    val customers by viewModel.allCustomers.collectAsState()
    val fullOrderDetails by viewModel.currentFullOrderDetails.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()

    // Estado para el diálogo de "Añadir Producto"
    var showAddProductDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearFormStates()
        }
    }

    Scaffold(
        // Mostramos el botón "+" solo si estamos editando un pedido
        floatingActionButton = {
            if (orderId != 0) {
                FloatingActionButton(onClick = { showAddProductDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplicamos el padding del Scaffold
                .padding(16.dp), // Añadimos nuestro propio padding
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (orderId == 0) "Nuevo Pedido" else "Editar Pedido",
                style = MaterialTheme.typography.headlineMedium
            )

            if (orderId != 0) {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                Text("Pedido #${formState.id}")
                Text("Fecha: ${sdf.format(formState.date)}")
            }

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
                // Si es un pedido nuevo, el botón lo crea y sale.
                // Si es un pedido existente, solo actualiza la cabecera (cliente/fecha)
                Text(if (orderId == 0) "Crear Pedido y Salir" else "Actualizar Cabecera")
            }

            // --- SECCIÓN DE DETALLES (SOLO PARA PEDIDOS EXISTENTES) ---
            if (orderId != 0) {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text("Productos en el Pedido", style = MaterialTheme.typography.headlineSmall)

                // Verificamos el estado de los detalles
                when {
                    fullOrderDetails == null -> {
                        // Está cargando
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    fullOrderDetails!!.details.isEmpty() -> {
                        // La lista está vacía
                        Text("Aún no hay productos. Añade uno con el botón '+'.")
                    }
                    else -> {
                        // Mostramos la lista de productos
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(fullOrderDetails!!.details) { detailWithProduct ->
                                OrderDetailRow(
                                    detail = detailWithProduct,
                                    onDelete = {
                                        viewModel.removeProductFromCurrentOrder(detailWithProduct.orderDetail)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // --- DIÁLOGO PARA AÑADIR PRODUCTO ---
    if (showAddProductDialog) {
        AddProductToOrderDialog(
            allProducts = allProducts,
            onDismiss = { showAddProductDialog = false },
            onConfirm = { productId, quantity ->
                viewModel.addProductToCurrentOrder(productId, quantity)
                showAddProductDialog = false
            }
        )
    }
}

/**
 * Muestra una fila con el producto, cantidad y un botón de borrar.
 */
@Composable
fun OrderDetailRow(
    detail: OrderDetailWithProduct,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(detail.product.ProductName, style = MaterialTheme.typography.bodyLarge)
                Text("S/ ${detail.product.Price} c/u", style = MaterialTheme.typography.bodySmall)
            }
            Text("Cant: ${detail.orderDetail.Quantity}", modifier = Modifier.padding(horizontal = 16.dp))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

/**
 * Diálogo para seleccionar un producto y una cantidad.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductToOrderDialog(
    allProducts: List<Product>,
    onDismiss: () -> Unit,
    onConfirm: (productId: Int, quantity: Int) -> Unit
) {
    var selectedProductId by remember { mutableStateOf(allProducts.firstOrNull()?.ProductID ?: 0) }
    var quantity by remember { mutableStateOf("1") }
    var expanded by remember { mutableStateOf(false) }

    val selectedProduct = allProducts.find { it.ProductID == selectedProductId }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Dropdown de Productos
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedProduct?.ProductName ?: "Seleccionar producto",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        allProducts.forEach { product ->
                            DropdownMenuItem(
                                text = { Text(product.ProductName) },
                                onClick = {
                                    selectedProductId = product.ProductID
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Campo de Cantidad
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { qty ->
                        // Permitir solo números
                        if (qty.all { it.isDigit() }) {
                            quantity = qty
                        }
                    },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val quantInt = quantity.toIntOrNull() ?: 0
                    if (selectedProductId != 0 && quantInt > 0) {
                        onConfirm(selectedProductId, quantInt)
                    }
                },
                enabled = (quantity.toIntOrNull() ?: 0) > 0 && selectedProductId != 0
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// El Composable CustomerDropdown no cambia, lo dejo por completitud
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
        onExpandedChange = { if (enabled) expanded = !expanded },
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