package com.idnp2025b.bodega.data

import com.idnp2025b.bodega.data.dao.BodegaDao
import com.idnp2025b.bodega.data.entities.Customer
import com.idnp2025b.bodega.data.entities.CustomerWithOrders
import com.idnp2025b.bodega.data.entities.FullOrderDetails
import com.idnp2025b.bodega.data.entities.Product
import kotlinx.coroutines.flow.Flow

/**
 * El Repositorio es la única fuente de datos para el ViewModel.
 * Oculta la complejidad de si los datos vienen del DAO o de una red.
 */
class BodegaRepository(private val bodegaDao: BodegaDao) {

    // --- READ (Flows para la UI) ---
    val allCustomers: Flow<List<Customer>> = bodegaDao.getAllCustomers()
    val allProducts: Flow<List<Product>> = bodegaDao.getAllProducts()

    // --- READ (Suspend para obtener datos específicos) ---
    suspend fun getCustomerWithOrders(id: Int): CustomerWithOrders? {
        return bodegaDao.getCustomerWithOrders(id)
    }

    suspend fun getFullOrderDetails(id: Int): FullOrderDetails? {
        return bodegaDao.getFullOrderDetails(id)
    }

    // --- CREATE, UPDATE, DELETE (Suspend functions) ---
    suspend fun insertCustomer(customer: Customer) {
        bodegaDao.insertCustomer(customer)
    }

    suspend fun updateCustomer(customer: Customer) {
        bodegaDao.updateCustomer(customer)
    }

    suspend fun deleteCustomer(customer: Customer) {
        bodegaDao.deleteCustomer(customer)
    }
}