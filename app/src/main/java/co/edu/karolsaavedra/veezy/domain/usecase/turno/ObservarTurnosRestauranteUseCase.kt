package co.edu.karolsaavedra.veezy.domain.usecase.turno

import co.edu.karolsaavedra.veezy.domain.repository.TurnoRepository

class ObservarTurnosRestauranteUseCase(private val repository: TurnoRepository) {
    operator fun invoke(nombreRestaurante: String) =
        repository.observarTurnosRestaurante(nombreRestaurante)
}