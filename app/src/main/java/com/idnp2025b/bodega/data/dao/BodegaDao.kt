package com.idnp2025b.bodega.data.dao

import androidx.room.*
import com.idnp2025b.bodega.data.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BodegaDao {

    // --- INSERT (CREATE) ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomer(customer: Customer)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrder(order: Order)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrderDetail(detail: OrderDetail)

    // --- Para la carga inicial ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomerList(customers: List<Customer>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryList(categories: List<Category>)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProductList(products: List<Product>)

    // --- READ (SELECTS) ---
    @Query("SELECT * FROM Customer")
    fun getAllCustomers(): Flow<List<Customer>>
    @Query("SELECT * FROM Product")
    fun getAllProducts(): Flow<List<Product>>
    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<Category>>
    @Query("SELECT * FROM `Order`")
    fun getAllOrders(): Flow<List<Order>>

    @Query("SELECT * FROM Customer WHERE CustomerID = :id")
    suspend fun getCustomerById(id: Int): Customer?
    @Query("SELECT * FROM Product WHERE ProductID = :id")
    suspend fun getProductById(id: Int): Product?
    @Query("SELECT * FROM `Order` WHERE OrderID = :id")
    suspend fun getOrderById(id: Int): Order?

    // --- READ (Relaciones) ---
    @Transaction
    @Query("SELECT * FROM Customer WHERE CustomerID = :customerId")
    suspend fun getCustomerWithOrders(customerId: Int): CustomerWithOrders?
    @Transaction
    @Query("SELECT * FROM Category WHERE CategoryID = :categoryId")
    suspend fun getCategoryWithProducts(categoryId: Int): CategoryWithProducts?
    @Transaction
    @Query("SELECT * FROM `Order` WHERE OrderID = :orderId")
    suspend fun getFullOrderDetails(orderId: Int): FullOrderDetails?

    // --- UPDATE ---
    @Update
    suspend fun updateCustomer(customer: Customer)
    @Update
    suspend fun updateProduct(product: Product)
    @Update
    suspend fun updateOrder(order: Order)
    @Update
    suspend fun updateOrderDetail(detail: OrderDetail)

    // --- DELETE ---
    @Delete
    suspend fun deleteCustomer(customer: Customer)
    @Delete
    suspend fun deleteProduct(product: Product)
    @Delete
    suspend fun deleteOrder(order: Order)
    @Delete
    suspend fun deleteOrderDetail(detail: OrderDetail)
}