package com.idnp2025b.bodega.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idnp2025b.bodega.data.entities.Category
import com.idnp2025b.bodega.viewmodel.BodegaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    navController: NavController,
    viewModel: BodegaViewModel,
    productId: Int
) {
    val formState = viewModel.productFormState
    val categories by viewModel.allCategories.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
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
            text = if (productId == 0) "Nuevo Producto" else "Editar Producto",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = formState.name,
            onValueChange = { viewModel.updateProductFormState(formState.copy(name = it)) },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.price,
            onValueChange = { viewModel.updateProductFormState(formState.copy(price = it)) },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown para Categorías
        CategoryDropdown(
            categories = categories,
            selectedCategoryId = formState.categoryId,
            onCategorySelected = {
                viewModel.updateProductFormState(formState.copy(categoryId = it))
            }
        )

        Button(
            onClick = {
                viewModel.saveProduct()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (productId == 0) "Crear" else "Actualizar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategoryId: Int,
    onCategorySelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedCategoryName = categories.find { it.CategoryID == selectedCategoryId }?.CategoryName ?: "Seleccionar categoría"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedCategoryName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.CategoryName) },
                    onClick = {
                        onCategorySelected(category.CategoryID)
                        expanded = false
                    }
                )
            }
        }
    }
}