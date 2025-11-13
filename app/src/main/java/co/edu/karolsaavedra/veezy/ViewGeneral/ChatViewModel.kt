package co.edu.karolsaavedra.veezy.ViewGeneral

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository = ChatRepository()) : ViewModel() {

    private val _mensajes = MutableStateFlow<List<Map<String, Any>>>(emptyList())
    val mensajes: StateFlow<List<Map<String, Any>>> = _mensajes

    private var chatIdActual: String? = null

    fun iniciarChat(
        clienteId: String,
        restauranteId: String,
        clienteNombre: String,
        restauranteNombre: String
    ) {
        viewModelScope.launch {
            val chatId = repository.makeChatId(clienteId, restauranteId)
            chatIdActual = chatId
            repository.obtenerOCrearChat(chatId, clienteId, restauranteId, clienteNombre, restauranteNombre)

            repository.escucharMensajes(chatId) { mensajes ->
                _mensajes.value = mensajes
            }
        }
    }

    fun enviarMensaje(texto: String, tipoEmisor: String) {
        viewModelScope.launch {
            chatIdActual?.let { repository.enviarMensaje(it, texto, tipoEmisor) }
        }
    }
}