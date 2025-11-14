package com.idnp2025b.bodega

import android.app.Application
import com.idnp2025b.bodega.data.BodegaDatabase
import com.idnp2025b.bodega.data.BodegaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BodegaApplication : Application() {

    // Un 'scope' global para la app
    val applicationScope = CoroutineScope(SupervisorJob())

    // Instancia 'lazy' de la BD y el Repo (solo se crean una vez)
    val database by lazy { BodegaDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { BodegaRepository(database.bodegaDao()) }
}