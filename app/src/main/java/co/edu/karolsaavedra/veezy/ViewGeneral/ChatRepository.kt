package co.edu.karolsaavedra.veezy.ViewGeneral


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ChatRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    /**
     * Genera un ID único de chat a partir de los dos UIDs.
     * Esto garantiza que el mismo par de usuarios siempre comparta el mismo chat.
     */
    fun makeChatId(uidA: String, uidB: String): String {
        return if (uidA < uidB) "${uidA}_${uidB}" else "${uidB}_${uidA}"
    }

    /**
     * Obtiene un chat existente o crea uno nuevo si no existe.
     */
    suspend fun obtenerOCrearChat(
        chatId: String,
        clienteId: String,
        restauranteId: String,
        clienteNombre: String,
        restauranteNombre: String
    ) {
        val chatRef = firestore.collection("chat").document(chatId) // 👈 tu colección se llama "chat"
        val snapshot = chatRef.get().await()

        if (!snapshot.exists()) {
            val meta = mapOf(
                "chatId" to chatId,
                "clienteId" to clienteId,
                "restauranteId" to restauranteId,
                "clienteNombre" to clienteNombre,
                "restauranteNombre" to restauranteNombre,
                "ultimoMensaje" to "",
                "timestamp" to FieldValue.serverTimestamp(),
                "participantes" to listOf(clienteId, restauranteId) // 👈 se agrega automáticamente
            )
            chatRef.set(meta).await()
        }
    }

    /**
     * Envía un nuevo mensaje en el chat y actualiza el último mensaje.
     */
    suspend fun enviarMensaje(
        chatId: String,
        texto: String,
        tipoEmisor: String
    ) {
        val uid = auth.currentUser?.uid ?: return
        val chatRef = firestore.collection("chat").document(chatId)

        val mensaje = mapOf(
            "texto" to texto,
            "emisorId" to uid,
            "tipoEmisor" to tipoEmisor,
            "timestamp" to FieldValue.serverTimestamp(),
            "leido" to false
        )

        // Guardar el mensaje en la subcolección "mensajes"
        chatRef.collection("mensajes").add(mensaje).await()

        // Actualizar metadatos del chat principal
        chatRef.update(
            mapOf(
                "ultimoMensaje" to texto,
                "timestamp" to FieldValue.serverTimestamp()
            )
        ).await()
    }

    /**
     * Escucha los mensajes en tiempo real dentro del chat.
     */
    fun escucharMensajes(
        chatId: String,
        onMensajesActualizados: (List<Map<String, Any>>) -> Unit
    ): ListenerRegistration {
        val query = firestore.collection("chat")
            .document(chatId)
            .collection("mensajes")
            .orderBy("timestamp", Query.Direction.ASCENDING)

        return query.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val lista = snapshot?.documents?.mapNotNull { it.data } ?: emptyList()
            onMensajesActualizados(lista)
        }
    }
}