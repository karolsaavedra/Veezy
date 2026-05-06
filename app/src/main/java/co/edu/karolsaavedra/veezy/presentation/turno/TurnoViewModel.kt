package co.edu.karolsaavedra.veezy.presentation.turno

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.karolsaavedra.veezy.domain.model.Turno
import co.edu.karolsaavedra.veezy.domain.usecase.turno.CrearTurnoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.turno.EliminarTurnoUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.turno.ObservarTurnosClienteUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.turno.ObservarTurnosRestauranteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TurnoUiState(
    val isLoading: Boolean = false,
    val turnos: List<Turno> = emptyList(),
    val turnoCreado: String? = null,
    val error: String? = null
)

class TurnoViewModel(
    private val crearTurnoUseCase: CrearTurnoUseCase,
    private val eliminarTurnoUseCase: EliminarTurnoUseCase,
    private val observarTurnosClienteUseCase: ObservarTurnosClienteUseCase,
    private val observarTurnosRestauranteUseCase: ObservarTurnosRestauranteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TurnoUiState())
    val uiState: StateFlow<TurnoUiState> = _uiState

    fun observarTurnosCliente(clienteId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            observarTurnosClienteUseCase(clienteId).collect { lista ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    turnos = lista
                )
            }
        }
    }

    fun observarTurnosRestaurante(nombreRestaurante: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            observarTurnosRestauranteUseCase(nombreRestaurante).collect { lista ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    turnos = lista
                )
            }
        }
    }

    fun crearTurno(turno: Turno) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = crearTurnoUseCase(turno)
            result.fold(
                onSuccess = { id ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        turnoCreado = id
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al crear turno"
                    )
                }
            )
        }
    }

    fun eliminarTurno(turnoId: String) {
        viewModelScope.launch {
            val result = eliminarTurnoUseCase(turnoId)
            result.onFailure {
                _uiState.value = _uiState.value.copy(
                    error = it.message ?: "Error al eliminar turno"
                )
            }
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun limpiarTurnoCreado() {
        _uiState.value = _uiState.value.copy(turnoCreado = null)
    }
}