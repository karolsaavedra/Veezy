package co.edu.karolsaavedra.veezy.domain.usecase.turno

import co.edu.karolsaavedra.veezy.domain.model.Turno
import co.edu.karolsaavedra.veezy.domain.repository.TurnoRepository

class CrearTurnoUseCase(private val repository: TurnoRepository) {
    suspend operator fun invoke(turno: Turno) =
        repository.crearTurno(turno)
}