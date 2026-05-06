package co.edu.karolsaavedra.veezy.domain.repository

import co.edu.karolsaavedra.veezy.domain.model.RolUsuario
import co.edu.karolsaavedra.veezy.domain.model.Usuario

interface AuthRepository {
    suspend fun loginCliente(email: String, password: String): Result<Usuario>
    suspend fun loginRestaurante(email: String, password: String): Result<Usuario>
    suspend fun registrarCliente(
        nombre: String,
        apellido: String,
        email: String,
        password: String
    ): Result<Usuario>
    suspend fun registrarRestaurante(
        nombre: String,
        direccion: String,
        horario: String,
        email: String,
        password: String
    ): Result<Usuario>
    suspend fun obtenerRolUsuarioActual(): Result<RolUsuario>
    fun cerrarSesion()
    fun getUidActual(): String?
}