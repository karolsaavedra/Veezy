package co.edu.karolsaavedra.veezy.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreHelper {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    //  Registrar CLIENTE
    fun registrarCliente(
        nombre: String,
        apellido: String,
        correo: String,
        codigoCliente: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val clienteData = hashMapOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "correo" to correo,
            "codigoCliente" to codigoCliente
        )

        db.collection("Clientes").document(codigoCliente)
            .set(clienteData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error desconocido") }
    }

    //  Registrar RESTAURANTE
    fun registrarRestaurante(
        nombre: String,
        direccion: String,
        correo: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val restauranteData = hashMapOf(
            "nombre" to nombre,
            "direccion" to direccion,
            "correo" to correo
        )

        db.collection("Restaurantes").document(nombre)
            .set(restauranteData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error desconocido") }
    }

    //  Guardar pedido de cliente (para llevar o restaurante)
    fun guardarPedidoCliente(
        codigoCliente: String,
        tipo: String, // "Para llevar" o "Restaurante"
        hamburguesas: Int,
        papas: Int,
        personas: Int? = null,
        turno: String = generarTurno(),
        tiempoEspera: String = generarTiempoEspera(),
        esParaLlevar: Boolean,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val pedido = hashMapOf(
            "tipo" to tipo,
            "hamburguesas" to hamburguesas,
            "papas" to papas,
            "turno" to turno,
            "tiempoEspera" to tiempoEspera,
            "esParaLlevar" to esParaLlevar,
            "codigoQR" to generarCodigoQR(),
        )

        if (!esParaLlevar && personas != null) {
            pedido["personas"] = personas
        }

        db.collection("Clientes").document(codigoCliente)
            .collection("Turnos")
            .add(pedido)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error desconocido") }
    }

    //  Obtener tipo de usuario
    fun obtenerTipoUsuario(
        correo: String,
        onResult: (String?) -> Unit
    ) {
        db.collection("Clientes").whereEqualTo("correo", correo).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    onResult("Cliente")
                    return@addOnSuccessListener
                }

                db.collection("Restaurantes").whereEqualTo("correo", correo).get()
                    .addOnSuccessListener { restauranteSnapshot ->
                        if (!restauranteSnapshot.isEmpty) {
                            onResult("Restaurante")
                        } else {
                            onResult(null)
                        }
                    }
            }
    }

    //  Generadores simulados (puedes mejorar con lógica real)
    private fun generarTurno(): String {
        val numero = (1000..9999).random()
        return "T$numero"
    }

    private fun generarTiempoEspera(): String {
        val minutos = (5..30).random()
        return "$minutos minutos"
    }

    private fun generarCodigoQR(): String {
        val letras = ('A'..'Z').random()
        val numeros = (1000..9999).random()
        return "$letras$numeros"
    }
}
