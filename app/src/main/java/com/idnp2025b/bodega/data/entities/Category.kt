package com.idnp2025b.bodega.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey
    val CategoryID: Int
)

// --- Conexión/Relación (1-a-N) ---
data class CategoryWithProducts(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "CategoryID",
        entityColumn = "CategoryID"
    )
    val products: List<Product>
)