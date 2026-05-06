package co.edu.karolsaavedra.veezy.domain.usecase.auth

import co.edu.karolsaavedra.veezy.domain.repository.AuthRepository

class RegistrarClienteUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        nombre: String,
        apellido: String,
        email: String,
        password: String
    ) = repository.registrarCliente(nombre, apellido, email, password)
}