package co.edu.karolsaavedra.veezy.domain.usecase.chat

import co.edu.karolsaavedra.veezy.domain.repository.ChatRepository

class EnviarMensajeUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String, texto: String) =
        repository.enviarMensaje(chatId, texto)
}