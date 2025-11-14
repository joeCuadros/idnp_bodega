package com.idnp2025b.bodega.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(
    tableName = "Order",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["CustomerID"],
            childColumns = ["CustomerID"]
        )
    ],
    indices = [Index(value = ["CustomerID"])]
)
data class Order(
    @PrimaryKey
    val OrderID: Int,
    val CustomerID: Int,
    val OrderDate: Date
)

@Entity(
    tableName = "OrderDetail",
    primaryKeys = ["OrderID", "ProductID"],
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["OrderID"],
            childColumns = ["OrderID"]
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["ProductID"],
            childColumns = ["ProductID"]
        )
    ],
    indices = [
        Index(value = ["OrderID"]),
        Index(value = ["ProductID"])
    ]
)
data class OrderDetail(
    val OrderID: Int,
    val ProductID: Int,
    val Quantity: Int
)

// --- Relaciones ---
data class OrderWithProducts(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "OrderID",
        entity = Product::class,
        entityColumn = "ProductID",
        associateBy = Junction(
            value = OrderDetail::class,
            parentColumn = "OrderID",
            entityColumn = "ProductID"
        )
    )
    val products: List<Product>
)

data class FullOrderDetails(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "OrderID",
        entity = OrderDetail::class,
        entityColumn = "OrderID"
    )
    val details: List<OrderDetailWithProduct>
)

data class OrderDetailWithProduct(
    @Embedded val orderDetail: OrderDetail,
    @Relation(
        parentColumn = "ProductID",
        entityColumn = "ProductID"
    )
    val product: Product
)