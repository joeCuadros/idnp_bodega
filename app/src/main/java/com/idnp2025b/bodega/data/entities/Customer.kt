package com.idnp2025b.bodega.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Customer")
data class Customer(
    @PrimaryKey
    val CustomerID: Int,
    val FirstName: String,
    val LastName: String,
    val Email: String
)

data class CustomerWithOrders(
    @Embedded val customer: Customer,
    @Relation(
        parentColumn = "CustomerID",
        entityColumn = "CustomerID"
    )
    val orders: List<Order>
)