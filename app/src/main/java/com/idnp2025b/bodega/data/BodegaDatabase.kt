package com.idnp2025b.bodega.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.idnp2025b.bodega.data.dao.BodegaDao
import com.idnp2025b.bodega.data.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Customer::class,
        Category::class,
        Product::class,
        Order::class,
        OrderDetail::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BodegaDatabase : RoomDatabase() {

    abstract fun bodegaDao(): BodegaDao

    companion object {
        @Volatile
        private var INSTANCE: BodegaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BodegaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BodegaDatabase::class.java,
                    "bodega_database"
                )
                    // Agrega el Callback para la carga inicial
                    .addCallback(BodegaDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // --- Callback para la carga inicial de datos (Manual) ---
    private class BodegaDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.bodegaDao())
                }
            }
        }

        suspend fun populateDatabase(dao: BodegaDao) {
            // Datos de ejemplo para la carga inicial
            val categories = listOf(
                Category(CategoryID = 1, CategoryName = "Bebidas"),
                Category(CategoryID = 2, CategoryName = "Snacks")
            )
            dao.insertCategoryList(categories)

            val products = listOf(
                Product(ProductID = 101, ProductName = "Inka Kola 1.5L", Price = 7.50, CategoryID = 1),
                Product(ProductID = 102, ProductName = "Papas Lays", Price = 3.00, CategoryID = 2),
                Product(ProductID = 103, ProductName = "Agua San Mateo", Price = 2.50, CategoryID = 1)
            )
            dao.insertProductList(products)

            val customers = listOf(
                Customer(CustomerID = 1, FirstName = "Juan", LastName = "Perez", Email = "juan@email.com"),
                Customer(CustomerID = 2, FirstName = "Maria", LastName = "Lopez", Email = "maria@email.com")
            )
            dao.insertCustomerList(customers)
        }
    }
}