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
    val OrderDate: Date // Requiere un TypeConverter
)

@Entity(
    tableName = "OrderDetail",
    primaryKeys = ["OrderID", "ProductID"], // Clave primaria compuesta
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


// --- Conexiones/Relaciones ---

// Conexión N-a-N (Pedido con sus Productos)
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

// Conexión para ver el pedido con el detalle (y la cantidad)
data class FullOrderDetails(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "OrderID",
        entity = OrderDetail::class,
        entityColumn = "OrderID"
    )
    val details: List<OrderDetailWithProduct>
)

// Clase auxiliar para la relación FullOrderDetails
data class OrderDetailWithProduct(
    @Embedded val orderDetail: OrderDetail,
    @Relation(
        parentColumn = "ProductID",
        entityColumn = "ProductID"
    )
    val product: Product
)