package co.edu.karolsaavedra.veezy.domain.usecase.chat

import co.edu.karolsaavedra.veezy.domain.repository.ChatRepository

class ObservarMensajesUseCase(private val repository: ChatRepository) {
    operator fun invoke(chatId: String) =
        repository.observarMensajes(chatId)
}