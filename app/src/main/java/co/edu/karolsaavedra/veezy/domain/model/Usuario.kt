package co.edu.karolsaavedra.veezy.domain.model

data class Usuario(
    val uid: String,
    val nombre: String,
    val apellido: String = "",
    val email: String,
    val rol: RolUsuario
)