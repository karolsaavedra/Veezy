package co.edu.karolsaavedra.veezy.domain.usecase.producto

import co.edu.karolsaavedra.veezy.domain.repository.ProductoRepository

class ObtenerTodosProductosUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke() =
        repository.obtenerTodosLosProductos()
}