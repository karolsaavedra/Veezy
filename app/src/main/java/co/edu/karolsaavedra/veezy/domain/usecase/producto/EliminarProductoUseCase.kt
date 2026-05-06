package co.edu.karolsaavedra.veezy.domain.usecase.producto

import co.edu.karolsaavedra.veezy.domain.repository.ProductoRepository

class EliminarProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(
        restauranteUid: String,
        productoId: String,
        imagenUrl: String
    ) = repository.eliminarProducto(restauranteUid, productoId, imagenUrl)
}