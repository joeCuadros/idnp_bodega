package com.idnp2025b.bodega.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey
    val CategoryID: Int,
    val CategoryName: String // Añadido para que la carga inicial tenga sentido
)

data class CategoryWithProducts(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "CategoryID",
        entityColumn = "CategoryID"
    )
    val products: List<Product>
)