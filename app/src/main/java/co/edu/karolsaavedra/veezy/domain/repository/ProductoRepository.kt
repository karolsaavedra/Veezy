package co.edu.karolsaavedra.veezy.domain.repository

import android.net.Uri
import co.edu.karolsaavedra.veezy.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun observarProductosRestaurante(restauranteUid: String): Flow<List<Producto>>
    suspend fun obtenerTodosLosProductos(): Result<List<Producto>>
    suspend fun agregarProducto(
        restauranteUid: String,
        producto: Producto,
        imagenUri: Uri
    ): Result<Unit>
    suspend fun eliminarProducto(
        restauranteUid: String,
        productoId: String,
        imagenUrl: String
    ): Result<Unit>
}