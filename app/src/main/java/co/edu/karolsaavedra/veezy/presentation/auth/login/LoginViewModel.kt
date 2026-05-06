package co.edu.karolsaavedra.veezy.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.karolsaavedra.veezy.domain.model.RolUsuario
import co.edu.karolsaavedra.veezy.domain.usecase.auth.LoginClienteUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.auth.LoginRestauranteUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.auth.ObtenerRolUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false,
    val rol: RolUsuario? = null
)

class LoginViewModel(
    private val loginClienteUseCase: LoginClienteUseCase,
    private val loginRestauranteUseCase: LoginRestauranteUseCase,
    private val obtenerRolUseCase: ObtenerRolUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun loginCliente(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = loginClienteUseCase(email, password)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginExitoso = true,
                        rol = RolUsuario.CLIENTE
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al iniciar sesión"
                    )
                }
            )
        }
    }

    fun loginRestaurante(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = loginRestauranteUseCase(email, password)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginExitoso = true,
                        rol = RolUsuario.RESTAURANTE
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message ?: "Error al iniciar sesión"
                    )
                }
            )
        }
    }

    fun obtenerRolActual() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = obtenerRolUseCase()
            result.fold(
                onSuccess = { rol ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        rol = rol
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = it.message
                    )
                }
            )
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}