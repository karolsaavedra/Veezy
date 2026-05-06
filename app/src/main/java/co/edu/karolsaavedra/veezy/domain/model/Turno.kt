package co.edu.karolsaavedra.veezy.domain.model

data class Turno(
    val id: String = "",
    val numero: Long = 0,
    val restauranteNombre: String = "",
    val clienteId: String = "",
    val tipo: TipoTurno = TipoTurno.RESTAURANTE,
    val estado: EstadoTurno = EstadoTurno.PENDIENTE,
    val timestamp: Long = 0L,
    val personas: Int = 0,
    val hamburguesas: Int = 0,
    val papas: Int = 0
)