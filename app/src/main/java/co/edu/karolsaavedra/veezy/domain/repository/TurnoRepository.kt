package co.edu.karolsaavedra.veezy.domain.repository

import co.edu.karolsaavedra.veezy.domain.model.Turno
import kotlinx.coroutines.flow.Flow

interface TurnoRepository {
    fun observarTurnosCliente(clienteId: String): Flow<List<Turno>>
    fun observarTurnosRestaurante(nombreRestaurante: String): Flow<List<Turno>>
    suspend fun crearTurno(turno: Turno): Result<String>
    suspend fun eliminarTurno(turnoId: String): Result<Unit>
    suspend fun obtenerTurno(turnoId: String): Result<Turno>
}