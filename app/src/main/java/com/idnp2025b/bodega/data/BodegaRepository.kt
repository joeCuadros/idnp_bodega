package com.idnp2025b.bodega.data

import com.idnp2025b.bodega.data.dao.BodegaDao
import com.idnp2025b.bodega.data.entities.*
import kotlinx.coroutines.flow.Flow

class BodegaRepository(private val bodegaDao: BodegaDao) {

    // --- READ (Flows) ---
    val allCustomers: Flow<List<Customer>> = bodegaDao.getAllCustomers()
    val allProducts: Flow<List<Product>> = bodegaDao.getAllProducts()
    val allCategories: Flow<List<Category>> = bodegaDao.getAllCategories()
    val allOrders: Flow<List<Order>> = bodegaDao.getAllOrders()

    // --- READ (Suspend) ---
    suspend fun getCustomerById(id: Int): Customer? = bodegaDao.getCustomerById(id)
    suspend fun getProductById(id: Int): Product? = bodegaDao.getProductById(id)
    suspend fun getOrderById(id: Int): Order? = bodegaDao.getOrderById(id)

    // --- CREATE ---
    suspend fun insertCustomer(customer: Customer) { bodegaDao.insertCustomer(customer) }
    suspend fun insertProduct(product: Product) { bodegaDao.insertProduct(product) }
    suspend fun insertOrder(order: Order) { bodegaDao.insertOrder(order) }

    // --- UPDATE ---
    suspend fun updateCustomer(customer: Customer) { bodegaDao.updateCustomer(customer) }
    suspend fun updateProduct(product: Product) { bodegaDao.updateProduct(product) }
    suspend fun updateOrder(order: Order) { bodegaDao.updateOrder(order) }

    // --- DELETE ---
    suspend fun deleteCustomer(customer: Customer) { bodegaDao.deleteCustomer(customer) }
    suspend fun deleteProduct(product: Product) { bodegaDao.deleteProduct(product) }
    suspend fun deleteOrder(order: Order) { bodegaDao.deleteOrder(order) }
}