package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

data class ChatItem(
    val id: String,
    val nombre: String,
    val ultimoMensaje: String = "",
    val chatId: String
)

@Composable
fun ChatListScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    var userName by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("") }
    var chatList by remember { mutableStateOf<List<ChatItem>>(emptyList()) }

    // 🔹 Obtener rol y nombre del usuario actual
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            val uid = currentUser.uid

            firestore.collection("clientes").document(uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        userRole = "cliente"
                        userName = doc.getString("nombre") ?: ""
                    } else {
                        firestore.collection("restaurantes").document(uid).get()
                            .addOnSuccessListener { rest ->
                                if (rest.exists()) {
                                    userRole = "restaurante"
                                    userName = rest.getString("nombre") ?: ""
                                }
                            }
                    }
                }
        }
    }

    // 🔹 Cargar lista según el rol
    LaunchedEffect(userRole, currentUser) {
        if (currentUser == null || userRole.isEmpty()) return@LaunchedEffect

        if (userRole == "cliente") {
            // ✅ CLIENTE: Mostrar todos los restaurantes disponibles
            firestore.collection("restaurantes")
                .addSnapshotListener { snapshot, _ ->
                    chatList = snapshot?.documents?.map { doc ->
                        val restauranteId = doc.id
                        val restauranteNombre = doc.getString("nombre") ?: "Restaurante"

                        // Generar el chatId
                        val chatId = if (currentUser.uid < restauranteId) {
                            "${currentUser.uid}_$restauranteId"
                        } else {
                            "${restauranteId}_${currentUser.uid}"
                        }

                        ChatItem(
                            id = restauranteId,
                            nombre = restauranteNombre,
                            chatId = chatId
                        )
                    } ?: emptyList()
                }
        } else {
            // ✅ RESTAURANTE: Mostrar solo chats donde participa
            firestore.collection("chat")
                .whereArrayContains("participantes", currentUser.uid)
                .addSnapshotListener { snapshot, _ ->
                    chatList = snapshot?.documents?.mapNotNull { doc ->
                        val data = doc.data ?: return@mapNotNull null
                        val clienteNombre = data["clienteNombre"] as? String ?: "Cliente"
                        val ultimoMensaje = data["ultimoMensaje"] as? String ?: ""

                        ChatItem(
                            id = doc.id,
                            nombre = clienteNombre,
                            ultimoMensaje = ultimoMensaje,
                            chatId = doc.id
                        )
                    } ?: emptyList()
                }
        }
    }

    // ===== UI =====
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7E5))
            .padding(16.dp)
    ) {
        Column {
            // Título
            Text(
                text = if (userRole == "cliente") "Iniciar Chat" else "Mis Chats",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontFamily = FontFamily(Font(R.font.afacad)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF000000)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (chatList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (userRole == "cliente") {
                            "No hay restaurantes disponibles."
                        } else {
                            "Aún no tienes chats.\nEspera a que un cliente te escriba."
                        },
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            color = Color.Gray
                        )
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatList) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val chatId = item.chatId

                                    // ✅ Verificar si el chat existe, si no crearlo
                                    firestore.collection("chat").document(chatId).get()
                                        .addOnSuccessListener { doc ->
                                            if (!doc.exists() && userRole == "cliente") {
                                                // Crear el chat si es cliente iniciando conversación
                                                val ids = chatId.split("_")
                                                val clienteId = ids[0]
                                                val restauranteId = ids[1]

                                                val nuevoChat = hashMapOf(
                                                    "chatId" to chatId,
                                                    "clienteId" to clienteId,
                                                    "restauranteId" to restauranteId,
                                                    "clienteNombre" to userName,
                                                    "restauranteNombre" to item.nombre,
                                                    "ultimoMensaje" to "",
                                                    "timestamp" to FieldValue.serverTimestamp(),
                                                    "participantes" to listOf(clienteId, restauranteId)
                                                )

                                                firestore.collection("chat").document(chatId)
                                                    .set(nuevoChat)
                                                    .addOnSuccessListener {
                                                        navController.navigate("chat_detail/$chatId")
                                                    }
                                            } else {
                                                // Chat ya existe, navegar directamente
                                                navController.navigate("chat_detail/$chatId")
                                            }
                                        }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        color = Color(0xFFFFC64F),
                                        shape = RoundedCornerShape(30.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // Información del chat
                            Column {
                                Text(
                                    text = item.nombre,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(R.font.afacad)),
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF000000)
                                    )
                                )

                                if (item.ultimoMensaje.isNotEmpty()) {
                                    Text(
                                        text = item.ultimoMensaje,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.afacad)),
                                            color = Color.Gray
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}