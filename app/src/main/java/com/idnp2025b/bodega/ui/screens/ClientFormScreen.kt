package com.idnp2025b.bodega.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idnp2025b.bodega.viewmodel.BodegaViewModel
import com.idnp2025b.bodega.viewmodel.ClientFormState

@Composable
fun ClientFormScreen(
    navController: NavController,
    viewModel: BodegaViewModel,
    clientId: Int
) {
    // Observa el estado del formulario en el ViewModel
    val formState = viewModel.clientFormState

    // Cargar los datos del cliente si es una edición (clientId != 0)
    // LaunchedEffect se ejecuta solo una vez cuando clientId cambia
    LaunchedEffect(clientId) {
        viewModel.loadClient(clientId)
    }

    // Limpiar el formulario al salir
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
            text = if (clientId == 0) "Nuevo Cliente" else "Editar Cliente",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = formState.firstName,
            onValueChange = { viewModel.updateClientFormState(formState.copy(firstName = it)) },
            label = { Text("Nombres") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.lastName,
            onValueChange = { viewModel.updateClientFormState(formState.copy(lastName = it)) },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = formState.email,
            onValueChange = { viewModel.updateClientFormState(formState.copy(email = it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.saveClient()
                navController.popBackStack() // Regresar a la lista
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (clientId == 0) "Crear" else "Actualizar")
        }
    }
}