package co.edu.karolsaavedra.veezy.data.repository

import co.edu.karolsaavedra.veezy.domain.model.Chat
import co.edu.karolsaavedra.veezy.domain.model.Mensaje
import co.edu.karolsaavedra.veezy.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ChatRepository {

    override fun makeChatId(uidA: String, uidB: String): String =
        if (uidA < uidB) "${uidA}_${uidB}" else "${uidB}_${uidA}"

    override suspend fun obtenerOCrearChat(
        chatId: String,
        clienteId: String,
        restauranteId: String,
        clienteNombre: String,
        restauranteNombre: String
    ): Result<Unit> {
        return try {
            val ref = firestore.collection("chat").document(chatId)
            val snap = ref.get().await()
            if (!snap.exists()) {
                val datos = mapOf(
                    "chatId" to chatId,
                    "clienteId" to clienteId,
                    "restauranteId" to restauranteId,
                    "clienteNombre" to clienteNombre,
                    "restauranteNombre" to restauranteNombre,
                    "ultimoMensaje" to "",
                    "timestamp" to FieldValue.serverTimestamp(),
                    "participantes" to listOf(clienteId, restauranteId)
                )
                ref.set(datos).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observarMensajes(chatId: String): Flow<List<Mensaje>> = callbackFlow {
        val listener = firestore.collection("chat")
            .document(chatId)
            .collection("mensajes")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    val texto = doc.getString("texto") ?: return@mapNotNull null
                    val emisorId = doc.getString("emisorId") ?: return@mapNotNull null
                    val ts = doc.getTimestamp("timestamp")?.toDate()?.time
                    Mensaje(texto = texto, emisorId = emisorId, timestamp = ts)
                } ?: emptyList()
                trySend(lista)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun enviarMensaje(chatId: String, texto: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("No autenticado"))
            val mensaje = mapOf(
                "texto" to texto,
                "emisorId" to uid,
                "timestamp" to FieldValue.serverTimestamp()
            )
            val ref = firestore.collection("chat").document(chatId)
            ref.collection("mensajes").add(mensaje).await()
            ref.update(
                mapOf(
                    "ultimoMensaje" to texto,
                    "timestamp" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observarChatsRestaurante(restauranteId: String): Flow<List<Chat>> = callbackFlow {
        val listener = firestore.collection("chat")
            .whereArrayContains("participantes", restauranteId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Chat(
                        chatId = doc.id,
                        clienteId = data["clienteId"] as? String ?: "",
                        restauranteId = data["restauranteId"] as? String ?: "",
                        clienteNombre = data["clienteNombre"] as? String ?: "",
                        restauranteNombre = data["restauranteNombre"] as? String ?: "",
                        ultimoMensaje = data["ultimoMensaje"] as? String ?: ""
                    )
                } ?: emptyList()
                trySend(lista)
            }
        awaitClose { listener.remove() }
    }
}