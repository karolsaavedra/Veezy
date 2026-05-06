package co.edu.karolsaavedra.veezy.data.repository

import co.edu.karolsaavedra.veezy.domain.model.EstadoTurno
import co.edu.karolsaavedra.veezy.domain.model.TipoTurno
import co.edu.karolsaavedra.veezy.domain.model.Turno
import co.edu.karolsaavedra.veezy.domain.repository.TurnoRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TurnoRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : TurnoRepository {

    private fun mapToTurno(id: String, data: Map<String, Any?>): Turno {
        val tipoStr = data["tipo"] as? String ?: "Restaurante"
        val estadoStr = data["estado"] as? String ?: "pendiente"
        val ts = data["timestamp"] as? Timestamp

        return Turno(
            id = id,
            numero = data["numero"] as? Long ?: 0L,
            restauranteNombre = data["restauranteNombre"] as? String ?: "",
            clienteId = data["clienteId"] as? String ?: "",
            tipo = if (tipoStr == "Para llevar") TipoTurno.PARA_LLEVAR else TipoTurno.RESTAURANTE,
            estado = when (estadoStr) {
                "atendido" -> EstadoTurno.ATENDIDO
                "cancelado" -> EstadoTurno.CANCELADO
                else -> EstadoTurno.PENDIENTE
            },
            timestamp = ts?.toDate()?.time ?: 0L,
            personas = (data["personas"] as? Long)?.toInt() ?: 0,
            hamburguesas = (data["hamburguesas"] as? Long)?.toInt() ?: 0,
            papas = (data["papas"] as? Long)?.toInt() ?: 0
        )
    }

    override fun observarTurnosCliente(clienteId: String): Flow<List<Turno>> = callbackFlow {
        val listener = firestore.collection("turnos")
            .whereEqualTo("clienteId", clienteId)
            .whereEqualTo("estado", "pendiente")
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.data?.let { mapToTurno(doc.id, it) }
                } ?: emptyList()
                trySend(lista)
            }
        awaitClose { listener.remove() }
    }

    override fun observarTurnosRestaurante(nombreRestaurante: String): Flow<List<Turno>> = callbackFlow {
        val listener = firestore.collection("turnos")
            .whereEqualTo("restauranteNombre", nombreRestaurante)
            .whereEqualTo("estado", "pendiente")
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.data?.let { mapToTurno(doc.id, it) }
                } ?: emptyList()
                trySend(lista)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun crearTurno(turno: Turno): Result<String> {
        return try {
            val tipoStr = if (turno.tipo == TipoTurno.PARA_LLEVAR) "Para llevar" else "Restaurante"
            val docs = firestore.collection("turnos")
                .whereEqualTo("restauranteNombre", turno.restauranteNombre)
                .whereEqualTo("tipo", tipoStr)
                .get().await()

            val ultimoNumero = docs.documents.mapNotNull {
                it.getLong("numero")
            }.maxOrNull() ?: 0L

            val datos = hashMapOf(
                "numero" to ultimoNumero + 1,
                "restauranteNombre" to turno.restauranteNombre,
                "tipo" to tipoStr,
                "clienteId" to turno.clienteId,
                "timestamp" to Timestamp.now(),
                "estado" to "pendiente",
                "personas" to turno.personas,
                "hamburguesas" to turno.hamburguesas,
                "papas" to turno.papas
            )
            val ref = firestore.collection("turnos").add(datos).await()
            Result.success(ref.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun eliminarTurno(turnoId: String): Result<Unit> {
        return try {
            firestore.collection("turnos").document(turnoId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun obtenerTurno(turnoId: String): Result<Turno> {
        return try {
            val doc = firestore.collection("turnos").document(turnoId).get().await()
            val data = doc.data ?: return Result.failure(Exception("Turno no encontrado"))
            Result.success(mapToTurno(doc.id, data))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}