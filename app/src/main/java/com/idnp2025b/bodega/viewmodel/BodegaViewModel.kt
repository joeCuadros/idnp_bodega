package com.idnp2025b.bodega.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.idnp2025b.bodega.data.BodegaRepository
import com.idnp2025b.bodega.data.entities.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

// --- Estados de UI para los formularios ---
data class ClientFormState(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = ""
)

data class ProductFormState(
    val id: Int = 0,
    val name: String = "",
    val price: String = "", // Usamos String para el TextField
    val categoryId: Int = 0
)

data class OrderFormState(
    val id: Int = 0,
    val customerId: Int = 0,
    val date: Date = Date()
)

class BodegaViewModel(private val repository: BodegaRepository) : ViewModel() {

    // --- LISTAS (READ) ---
    val allCustomers: StateFlow<List<Customer>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allCategories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allOrders: StateFlow<List<Order>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- ESTADO DE FORMULARIOS ---
    var clientFormState by mutableStateOf(ClientFormState())
        private set
    var productFormState by mutableStateOf(ProductFormState())
        private set
    var orderFormState by mutableStateOf(OrderFormState())
        private set

    // --- LÓGICA DE CLIENTES (CRUD) ---
    fun loadClient(id: Int) {
        viewModelScope.launch {
            val client = repository.getCustomerById(id) ?: ClientFormState().let {
                Customer(it.id, it.firstName, it.lastName, it.email)
            }
            clientFormState = ClientFormState(
                id = client.CustomerID,
                firstName = client.FirstName,
                lastName = client.LastName,
                email = client.Email
            )
        }
    }
    fun updateClientFormState(state: ClientFormState) { clientFormState = state }
    fun saveClient() = viewModelScope.launch {
        val client = Customer(
            CustomerID = clientFormState.id,
            FirstName = clientFormState.firstName,
            LastName = clientFormState.lastName,
            Email = clientFormState.email
        )
        if (client.CustomerID == 0) { // ID 0 significa NUEVO
            // Simular autoincremento (Room no lo hace con ID 0, lo haría con @Insert)
            // Esto es una simplificación, idealmente el ID sería autogenerado en la Entidad
            val newId = (allCustomers.value.maxOfOrNull { it.CustomerID } ?: 0) + 1
            repository.insertCustomer(client.copy(CustomerID = newId))
        } else {
            repository.updateCustomer(client) // ID existente, ACTUALIZAR
        }
    }
    fun deleteClient(customer: Customer) = viewModelScope.launch {
        repository.deleteCustomer(customer)
    }

    // --- LÓGICA DE PRODUCTOS (CRUD) ---
    fun loadProduct(id: Int) {
        viewModelScope.launch {
            val product = repository.getProductById(id) ?: ProductFormState().let {
                Product(it.id, it.name, it.price.toDoubleOrNull() ?: 0.0, it.categoryId)
            }
            productFormState = ProductFormState(
                id = product.ProductID,
                name = product.ProductName,
                price = product.Price.toString(),
                categoryId = product.CategoryID
            )
        }
    }
    fun updateProductFormState(state: ProductFormState) { productFormState = state }
    fun saveProduct() = viewModelScope.launch {
        val product = Product(
            ProductID = productFormState.id,
            ProductName = productFormState.name,
            Price = productFormState.price.toDoubleOrNull() ?: 0.0,
            CategoryID = productFormState.categoryId
        )
        if (product.ProductID == 0) {
            val newId = (allProducts.value.maxOfOrNull { it.ProductID } ?: 0) + 1
            repository.insertProduct(product.copy(ProductID = newId))
        } else {
            repository.updateProduct(product)
        }
    }
    fun deleteProduct(product: Product) = viewModelScope.launch {
        repository.deleteProduct(product)
    }

    // --- LÓGICA DE PEDIDOS (CRUD) ---
    fun loadOrder(id: Int) {
        viewModelScope.launch {
            val order = repository.getOrderById(id) ?: OrderFormState().let {
                Order(it.id, it.customerId, it.date)
            }
            orderFormState = OrderFormState(
                id = order.OrderID,
                customerId = order.CustomerID,
                date = order.OrderDate
            )
        }
    }
    fun updateOrderFormState(state: OrderFormState) { orderFormState = state }
    fun saveOrder() = viewModelScope.launch {
        val order = Order(
            OrderID = orderFormState.id,
            CustomerID = orderFormState.customerId,
            OrderDate = orderFormState.date // Para "Crear" usamos la fecha actual
        )
        if (order.OrderID == 0) {
            val newId = (allOrders.value.maxOfOrNull { it.OrderID } ?: 0) + 1
            repository.insertOrder(order.copy(OrderID = newId, OrderDate = Date())) // Pone fecha actual al crear
        } else {
            repository.updateOrder(order) // Actualiza (mantiene fecha original)
        }
    }
    fun deleteOrder(order: Order) = viewModelScope.launch {
        repository.deleteOrder(order)
    }

    // Limpia el estado del formulario al salir
    fun clearFormStates() {
        clientFormState = ClientFormState()
        productFormState = ProductFormState()
        orderFormState = OrderFormState()
    }
}

// Factory (igual que antes)
class BodegaViewModelFactory(private val repository: BodegaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BodegaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BodegaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}