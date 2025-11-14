package com.idnp2025b.bodega.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.idnp2025b.bodega.ui.screens.*
import com.idnp2025b.bodega.viewmodel.BodegaViewModel

// 1. Definimos las rutas (pantallas)
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ClientList : Screen("client_list")
    object ClientForm : Screen("client_form/{id}") {
        fun createRoute(id: Int) = "client_form/$id"
    }
    object ProductList : Screen("product_list")
    object ProductForm : Screen("product_form/{id}") {
        fun createRoute(id: Int) = "product_form/$id"
    }
    object OrderList : Screen("order_list")
    object OrderForm : Screen("order_form/{id}") {
        fun createRoute(id: Int) = "order_form/$id"
    }
}

// 2. Creamos el Host de Navegación
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: BodegaViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        // Pantalla Principal (Menú)
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // --- CRUD Clientes ---
        composable(Screen.ClientList.route) {
            ClientListScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.ClientForm.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            ClientFormScreen(navController = navController, viewModel = viewModel, clientId = id)
        }

        // --- CRUD Productos ---
        composable(Screen.ProductList.route) {
            ProductListScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.ProductForm.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            ProductFormScreen(navController = navController, viewModel = viewModel, productId = id)
        }

        // --- CRUD Pedidos ---
        composable(Screen.OrderList.route) {
            OrderListScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.OrderForm.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            OrderFormScreen(navController = navController, viewModel = viewModel, orderId = id)
        }
    }
}