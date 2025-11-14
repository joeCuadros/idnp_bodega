package com.idnp2025b.bodega.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.idnp2025b.bodega.data.BodegaRepository
import com.idnp2025b.bodega.data.entities.Customer
import com.idnp2025b.bodega.data.entities.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BodegaViewModel(private val repository: BodegaRepository) : ViewModel() {

    // --- EXPOSICIÓN DE DATOS (READ) ---

    // Convierte el Flow de Room en un StateFlow que Compose puede 'colectar'
    val allCustomers: StateFlow<List<Customer>> = repository.allCustomers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Inicia después de 5 seg
            initialValue = emptyList() // Valor inicial mientras carga
        )

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- ACCIONES (CREATE, UPDATE, DELETE) ---

    // Inserta un nuevo cliente (CREATE)
    fun insertCustomer(customer: Customer) = viewModelScope.launch {
        repository.insertCustomer(customer)
    }

    // Borra un cliente (DELETE)
    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        repository.deleteCustomer(customer)
    }
}

/**
 * Factory necesario para poder inyectar el Repositorio en el ViewModel
 */
class BodegaViewModelFactory(private val repository: BodegaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BodegaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BodegaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}