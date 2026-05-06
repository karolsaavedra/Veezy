package co.edu.karolsaavedra.veezy.domain.repository

import co.edu.karolsaavedra.veezy.domain.model.Restaurante

interface RestauranteRepository {
    suspend fun obtenerRestaurante(uid: String): Result<Restaurante>
    suspend fun obtenerTodosRestaurantes(): Result<List<Restaurante>>
    suspend fun actualizarRestaurante(
        uid: String,
        datos: Map<String, Any>
    ): Result<Unit>
}