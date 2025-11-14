package com.idnp2025b.bodega.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idnp2025b.bodega.data.dao.BodegaDao
import com.idnp2025b.bodega.data.entities.*

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

        fun getDatabase(context: Context): BodegaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BodegaDatabase::class.java,
                    "bodega_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}