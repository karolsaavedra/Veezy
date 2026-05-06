package co.edu.karolsaavedra.veezy.domain.usecase.auth

import co.edu.karolsaavedra.veezy.domain.repository.AuthRepository

class LoginRestauranteUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) =
        repository.loginRestaurante(email, password)
}