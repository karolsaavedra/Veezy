package co.edu.karolsaavedra.veezy.domain.usecase.turno

import co.edu.karolsaavedra.veezy.domain.repository.TurnoRepository

class ObservarTurnosClienteUseCase(private val repository: TurnoRepository) {
    operator fun invoke(clienteId: String) =
        repository.observarTurnosCliente(clienteId)
}