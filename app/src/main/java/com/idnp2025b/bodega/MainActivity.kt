package com.idnp2025b.bodega

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.idnp2025b.bodega.ui.screens.MainScreen
import com.idnp2025b.bodega.ui.theme.BodegaTheme
import com.idnp2025b.bodega.viewmodel.BodegaViewModel
import com.idnp2025b.bodega.viewmodel.BodegaViewModelFactory

class MainActivity : ComponentActivity() {

    // Inyectamos el ViewModel usando el Factory
    private val viewModel: BodegaViewModel by viewModels {
        BodegaViewModelFactory((application as BodegaApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodegaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a la pantalla principal y le pasamos el ViewModel
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}