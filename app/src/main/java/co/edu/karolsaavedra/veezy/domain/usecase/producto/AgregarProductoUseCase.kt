package co.edu.karolsaavedra.veezy.domain.usecase.producto

import android.net.Uri
import co.edu.karolsaavedra.veezy.domain.model.Producto
import co.edu.karolsaavedra.veezy.domain.repository.ProductoRepository

class AgregarProductoUseCase(private val repository: ProductoRepository) {
    suspend operator fun invoke(
        restauranteUid: String,
        producto: Producto,
        imagenUri: Uri
    ) = repository.agregarProducto(restauranteUid, producto, imagenUri)
}