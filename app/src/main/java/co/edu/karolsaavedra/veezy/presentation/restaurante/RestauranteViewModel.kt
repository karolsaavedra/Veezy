package co.edu.karolsaavedra.veezy.presentation.restaurante

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.karolsaavedra.veezy.domain.model.Restaurante
import co.edu.karolsaavedra.veezy.domain.repository.RestauranteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RestauranteUiState(
    val isLoading: Boolean = false,
    val restaurante: Restaurante? = null,
    val restaurantes: List<Restaurante> = emptyList(),
    val error: String? = null,
    val actualizacionExitosa: Boolean = false
)

class RestauranteViewModel(
    private val restauranteRepository: RestauranteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestauranteUiState())
    val uiState: StateFlow<RestauranteUiState> = _uiState

    fun cargarRestaurante(uid: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = restauranteRepository.obtenerRestaurante(uid)
            result.fold(
                onSuccess = { restaurante ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        restaurante = restaurante
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al cargar restaurante"
                    )
                }
            )
        }
    }

    fun cargarTodosRestaurantes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = restauranteRepository.obtenerTodosRestaurantes()
            result.fold(
                onSuccess = { lista ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        restaurantes = lista
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al cargar restaurantes"
                    )
                }
            )
        }
    }

    fun actualizarRestaurante(uid: String, datos: Map<String, Any>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = restauranteRepository.actualizarRestaurante(uid, datos)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        actualizacionExitosa = true
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al actualizar"
                    )
                }
            )
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}