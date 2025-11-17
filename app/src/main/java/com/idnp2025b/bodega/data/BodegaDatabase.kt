package com.idnp2025b.bodega.data

import android.content.Context
import android.widget.Toast // <-- 1. IMPORTAR TOAST
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.idnp2025b.bodega.data.dao.BodegaDao
import com.idnp2025b.bodega.data.entities.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext // <-- 2. IMPORTAR withContext
import java.io.IOException

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
                    .addCallback(BodegaDatabaseCallback(context.applicationContext, scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // --- Callback para la carga inicial de datos (desde JSON) ---
    private class BodegaDatabaseCallback(
        private val context: Context,
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {

                    // 3. Capturamos el resultado (true/false)
                    val success = populateDatabase(database.bodegaDao())

                    val message = if (success) {
                        "Datos iniciales (JSON) cargados con éxito."
                    } else {
                        "Error al cargar datos iniciales (JSON)."
                    }

                    // 4. Mostramos el Toast en el Hilo Principal
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // 5. Modificamos la función para que devuelva un Boolean
        suspend fun populateDatabase(dao: BodegaDao): Boolean {
            val gson = Gson()
            var allSuccess = true // Asumimos éxito al inicio

            // Cargar Categorías
            try {
                val categoriesJson = context.assets.open("categories.json")
                    .bufferedReader().use { it.readText() }

                val categoryListType = object : TypeToken<List<Category>>() {}.type
                val categories: List<Category> = gson.fromJson(categoriesJson, categoryListType)

                dao.insertCategoryList(categories)

            } catch (e: IOException) {
                e.printStackTrace()
                allSuccess = false // Si algo falla, lo marcamos
            }

            // Cargar Productos
            try {
                val productsJson = context.assets.open("products.json")
                    .bufferedReader().use { it.readText() }

                val productListType = object : TypeToken<List<Product>>() {}.type
                val products: List<Product> = gson.fromJson(productsJson, productListType)

                dao.insertProductList(products)
            } catch (e: IOException) {
                e.printStackTrace()
                allSuccess = false
            }

            // Cargar Clientes
            try {
                val customersJson = context.assets.open("customers.json")
                    .bufferedReader().use { it.readText() }

                val customerListType = object : TypeToken<List<Customer>>() {}.type
                val customers: List<Customer> = gson.fromJson(customersJson, customerListType)

                dao.insertCustomerList(customers)
            } catch (e: IOException) {
                e.printStackTrace()
                allSuccess = false
            }

            // 6. Devolvemos el resultado final
            return allSuccess
        }
    }
}