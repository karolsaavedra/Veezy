package co.edu.karolsaavedra.veezy.domain.model

data class Mensaje(
    val texto: String = "",
    val emisorId: String = "",
    val timestamp: Long? = null
)