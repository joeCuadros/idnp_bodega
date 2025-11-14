package com.idnp2025b.bodega.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Product",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["CategoryID"],
            childColumns = ["CategoryID"]
        )
    ],
    indices = [Index(value = ["CategoryID"])]
)
data class Product(
    @PrimaryKey
    val ProductID: Int,
    val ProductName: String,
    val Price: Double, // 'decimal' se mapea a Double
    val CategoryID: Int
)