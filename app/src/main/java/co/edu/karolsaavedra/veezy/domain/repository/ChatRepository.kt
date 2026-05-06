package co.edu.karolsaavedra.veezy.domain.repository

import co.edu.karolsaavedra.veezy.domain.model.Chat
import co.edu.karolsaavedra.veezy.domain.model.Mensaje
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun obtenerOCrearChat(
        chatId: String,
        clienteId: String,
        restauranteId: String,
        clienteNombre: String,
        restauranteNombre: String
    ): Result<Unit>
    fun observarMensajes(chatId: String): Flow<List<Mensaje>>
    suspend fun enviarMensaje(chatId: String, texto: String): Result<Unit>
    fun observarChatsRestaurante(restauranteId: String): Flow<List<Chat>>
    fun makeChatId(uidA: String, uidB: String): String
}