package co.edu.karolsaavedra.veezy.domain.usecase.auth

import co.edu.karolsaavedra.veezy.domain.repository.AuthRepository

class ObtenerRolUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.obtenerRolUsuarioActual()
}