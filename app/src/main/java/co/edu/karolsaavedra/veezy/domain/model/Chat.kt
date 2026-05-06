package co.edu.karolsaavedra.veezy.domain.model

data class Chat(
    val chatId: String = "",
    val clienteId: String = "",
    val restauranteId: String = "",
    val clienteNombre: String = "",
    val restauranteNombre: String = "",
    val ultimoMensaje: String = ""
)