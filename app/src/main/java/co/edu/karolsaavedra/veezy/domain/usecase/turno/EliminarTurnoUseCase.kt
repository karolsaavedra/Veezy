package co.edu.karolsaavedra.veezy.domain.usecase.turno

import co.edu.karolsaavedra.veezy.domain.repository.TurnoRepository

class EliminarTurnoUseCase(private val repository: TurnoRepository) {
    suspend operator fun invoke(turnoId: String) =
        repository.eliminarTurno(turnoId)
}