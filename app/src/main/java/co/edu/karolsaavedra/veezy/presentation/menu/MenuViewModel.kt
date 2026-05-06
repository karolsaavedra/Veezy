package co.edu.karolsaavedra.veezy.presentation.menu

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.karolsaavedra.veezy.domain.model.Producto
import co.edu.karolsaavedra.veezy.domain.usecase.producto.AgregarProductoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.producto.EliminarProductoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.producto.ObtenerTodosProductosUseCase
import co.edu.karolsaavedra.veezy.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MenuUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val error: String? = null,
    val operacionExitosa: Boolean = false
)

class MenuViewModel(
    private val obtenerTodosProductosUseCase: ObtenerTodosProductosUseCase,
    private val agregarProductoUseCase: AgregarProductoUseCase,
    private val eliminarProductoUseCase: EliminarProductoUseCase,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState

    fun cargarTodosLosProductos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = obtenerTodosProductosUseCase()
            result.fold(
                onSuccess = { lista ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        productos = lista
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al cargar productos"
                    )
                }
            )
        }
    }

    fun observarProductosRestaurante(restauranteUid: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            productoRepository.observarProductosRestaurante(restauranteUid).collect { lista ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    productos = lista
                )
            }
        }
    }

    fun agregarProducto(restauranteUid: String, producto: Producto, imagenUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = agregarProductoUseCase(restauranteUid, producto, imagenUri)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        operacionExitosa = true
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al agregar producto"
                    )
                }
            )
        }
    }

    fun eliminarProducto(restauranteUid: String, productoId: String, imagenUrl: String) {
        viewModelScope.launch {
            val result = eliminarProductoUseCase(restauranteUid, productoId, imagenUrl)
            result.onFailure {
                _uiState.value = _uiState.value.copy(
                    error = it.message ?: "Error al eliminar producto"
                )
            }
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun limpiarOperacion() {
        _uiState.value = _uiState.value.copy(operacionExitosa = false)
    }
}