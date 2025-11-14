package com.idnp2025b.bodega

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding // Importante
import androidx.compose.material3.* // Importante
import androidx.compose.runtime.Composable // Importante
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.idnp2025b.bodega.ui.navigation.AppNavigation
import com.idnp2025b.bodega.ui.theme.BodegaTheme
import com.idnp2025b.bodega.viewmodel.BodegaViewModel
import com.idnp2025b.bodega.viewmodel.BodegaViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: BodegaViewModel by viewModels {
        BodegaViewModelFactory((application as BodegaApplication).repository)
    }

    // Asegúrate de tener esta anotación para TopAppBar
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodegaTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Gestión Bodega") })
                    }
                ) { paddingValues -> // <-- LA VARIABLE SE LLAMA 'paddingValues'
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues), // <-- USA 'paddingValues' AQUÍ
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}