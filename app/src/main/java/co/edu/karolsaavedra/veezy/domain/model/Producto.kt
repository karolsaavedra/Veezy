package co.edu.karolsaavedra.veezy.domain.model

data class Producto(
    val id: String = "",
    val nombreProducto: String = "",
    val precio: String = "",
    val descripcion: String = "",
    val direccion: String = "",
    val imagenUrl: String = "",
    val nombreRestaurante: String = "",
    val horario: String = ""
)