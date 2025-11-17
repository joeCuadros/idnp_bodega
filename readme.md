# Proyecto: Gestión de Bodega (IDNP)
Aplicación Android nativa desarrollada para el curso de Ingeniería de Dispositivos Móviles (IDNP). El proyecto es un sistema de gestión para una bodega que permite administrar clientes, productos y pedidos para ventas a domicilio.

## Integrantes
* CHIRINOS NEGRON SEBASTIAN ARLEY
* CUADROS AMANQUI JOE JHONNY
* MARRON CARCAUSTO DANIEL ENRIQUE
* MARRON LOPE MISAEL JOSIAS
* VIZA CUTI RODRIGO ESTEFANO


## Funcionalidades Implementadas (Actividad)
Este proyecto cumple con los requisitos clave solicitados, enfocándose en la persistencia de datos y las operaciones de bases de datos relacionales.

### 1. Carga de Datos Inicial desde Archivos

Para asegurar que la aplicación contenga datos de ejemplo al ser instalada por primera vez, se implementó un sistema de carga automática.

* **Ubicación:** Los datos se encuentran en archivos JSON (`categories.json`, `products.json`, `customers.json`) dentro de la carpeta `assets/` del proyecto.
* **Implementación:** Se utiliza un `RoomDatabase.Callback()` dentro de la clase `BodegaDatabase.kt`.
* **Proceso:**
    1.  Cuando la base de datos se crea por primera vez (`onCreate`), el *callback* se activa.
    2.  Se lanza una corrutina en `Dispatchers.IO`.
    3.  Usando el `Context` de la aplicación, se leen los archivos JSON desde `assets`.
    4.  La librería **Gson** se utiliza para parsear (convertir) el texto JSON en listas de objetos Kotlin (ej. `List<Product>`).
    5.  Estos objetos se insertan en sus respectivas tablas usando los métodos del DAO (ej. `dao.insertProductList(...)`).

### 2. Operaciones CRUD y Consultas Relacionales
La aplicación soporta operaciones completas de Crear, Leer, Actualizar y Borrar (CRUD) para todas las entidades principales. El enfoque principal está en cómo se manejan las relaciones de la base de datos.

* **CRUD Básico:** Se implementa la gestión completa para Clientes, Productos y Pedidos.
* **Consulta 1-a-N (Uno-a-Muchos):** Esta relación se establece, por ejemplo, entre `Customer` y `Order`. Un cliente puede tener múltiples pedidos. La llave foránea `CustomerID` en la tabla `Order` es la que implementa esta relación.
* **Consulta N-a-M (Muchos-a-Muchos):** Esta es la relación más compleja, implementada entre `Order` y `Product` (un pedido puede tener muchos productos, y un producto puede estar en muchos pedidos).
    * **Tabla de Unión (Junction):** Se creó la entidad `OrderDetail` para que actúe como la tabla asociativa. Almacena el `OrderID`, `ProductID` y la `Quantity`.
    * **Clase de Relación:** Se creó la clase `FullOrderDetails` que usa las anotaciones `@Relation` de Room.
    * **Consulta:** Mediante una consulta transaccional (`@Transaction`), Room es capaz de obtener un `Order` y, usando la tabla `OrderDetail` como puente (`associateBy = Junction(...)`), traer la lista completa de `Product` asociados a ese pedido, permitiendo un manejo completo del CRUD de detalles de la orden.

## Arquitectura y Tecnologías

El proyecto sigue una arquitectura MVVM (Model-View-ViewModel) y utiliza las siguientes tecnologías:

* **Lenguaje:** 100% Kotlin
* **UI:** Jetpack Compose (declarativa)
* **Arquitectura:** MVVM
* **Base de Datos:** Room (para persistencia local SQLite)
* **Asincronía:** Kotlin Coroutines y Flow
* **Patrón:** Repositorio (para abstraer el origen de datos)
* **Navegación:** Jetpack Navigation for Compose
* **Inyección de Dependencias:** Manual (Service Locator) a través de la clase `BodegaApplication`.


## Estructura del Proyecto

* **`data/`**: Contiene toda la lógica de datos.
    * **`entities/`**: Define las tablas (`@Entity`) y las clases de relación.
    * **`dao/`**: Interfaz (`@Dao`) con las consultas SQL.
    * **`BodegaDatabase.kt`**: Clase principal de Room (`@Database`) que incluye el *callback* para la carga de datos.
    * **`BodegaRepository.kt`**: Repositorio que centraliza el acceso a los datos.
* **`viewmodel/`**: El `BodegaViewModel` que maneja el estado de la UI y la lógica de negocio.
* **`ui/`**: Paquete con todos los Composable.
    * **`screens/`**: Define cada pantalla de la aplicación.
    * **`navigation/`**: Define el `NavHost` y las rutas de navegación.