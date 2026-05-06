package co.edu.karolsaavedra.veezy.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.karolsaavedra.veezy.domain.model.Chat
import co.edu.karolsaavedra.veezy.domain.model.Mensaje
import co.edu.karolsaavedra.veezy.domain.usecase.chat.EnviarMensajeUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.chat.IniciarChatUseCase
import co.edu.karolsaavedra.veezy.domain.usecase.chat.ObservarMensajesUseCase
import co.edu.karolsaavedra.veezy.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val isLoading: Boolean = false,
    val mensajes: List<Mensaje> = emptyList(),
    val chats: List<Chat> = emptyList(),
    val error: String? = null,
    val mensajeEnviado: Boolean = false
)

class ChatViewModel(
    private val enviarMensajeUseCase: EnviarMensajeUseCase,
    private val iniciarChatUseCase: IniciarChatUseCase,
    private val observarMensajesUseCase: ObservarMensajesUseCase,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun iniciarYObservarChat(
        clienteId: String,
        restauranteId: String,
        clienteNombre: String,
        restauranteNombre: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val chatId = chatRepository.makeChatId(clienteId, restauranteId)
            iniciarChatUseCase(chatId, clienteId, restauranteId, clienteNombre, restauranteNombre)
            observarMensajesUseCase(chatId).collect { lista ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    mensajes = lista
                )
            }
        }
    }

    fun observarMensajes(chatId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            observarMensajesUseCase(chatId).collect { lista ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    mensajes = lista
                )
            }
        }
    }

    fun observarChatsRestaurante(restauranteId: String) {
        viewModelScope.launch {
            chatRepository.observarChatsRestaurante(restauranteId).collect { lista ->
                _uiState.value = _uiState.value.copy(chats = lista)
            }
        }
    }

    fun enviarMensaje(chatId: String, texto: String) {
        viewModelScope.launch {
            val result = enviarMensajeUseCase(chatId, texto)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(mensajeEnviado = true)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        error = it.message ?: "Error al enviar mensaje"
                    )
                }
            )
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}