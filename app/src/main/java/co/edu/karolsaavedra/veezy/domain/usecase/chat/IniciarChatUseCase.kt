package co.edu.karolsaavedra.veezy.domain.usecase.chat

import co.edu.karolsaavedra.veezy.domain.repository.ChatRepository

class IniciarChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(
        chatId: String,
        clienteId: String,
        restauranteId: String,
        clienteNombre: String,
        restauranteNombre: String
    ) = repository.obtenerOCrearChat(
        chatId, clienteId, restauranteId, clienteNombre, restauranteNombre
    )
}