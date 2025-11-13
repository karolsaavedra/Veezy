package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class ChatItemDisplay(
    val id: String,
    val nombre: String,
    val ultimoMensaje: String = "",
    val chatId: String
)

@Composable
fun ChatScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    var userName by remember { mutableStateOf<String?>(null) }
    var userRole by remember { mutableStateOf<String?>(null) }
    var displayList by remember { mutableStateOf<List<ChatItemDisplay>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // 🔹 1. Determinar rol del usuario
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            println("🔍 Buscando usuario: $uid")

            firestore.collection("clientes").document(uid).get()
                .addOnSuccessListener { clienteDoc ->
                    if (clienteDoc.exists()) {
                        userName = clienteDoc.getString("nombre")
                        userRole = "cliente"
                        println("✅ Usuario es CLIENTE: $userName")
                    } else {
                        firestore.collection("restaurantes").document(uid).get()
                            .addOnSuccessListener { restauranteDoc ->
                                if (restauranteDoc.exists()) {
                                    userName = restauranteDoc.getString("nombre")
                                    userRole = "restaurante"
                                    println("✅ Usuario es RESTAURANTE: $userName")
                                } else {
                                    println("❌ Usuario no encontrado en ninguna colección")
                                }
                            }
                            .addOnFailureListener { e ->
                                println("❌ Error buscando en restaurantes: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    println("❌ Error buscando en clientes: ${e.message}")
                }
        }
    }

    // 🔹 2. Cargar lista según el rol
    LaunchedEffect(userRole, currentUser) {
        if (currentUser == null || userRole == null) return@LaunchedEffect

        val uid = currentUser.uid
        isLoading = true

        if (userRole == "cliente") {
            // ✅ CLIENTE: Mostrar TODOS los restaurantes + último mensaje si existe chat
            println("📋 Cargando lista de restaurantes con últimos mensajes...")

            firestore.collection("restaurantes")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        println("❌ Error cargando restaurantes: ${error.message}")
                        displayList = emptyList()
                        isLoading = false
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        println("📦 ${snapshot.documents.size} restaurantes encontrados")

                        // Para cada restaurante, buscar si existe un chat
                        val restaurantes = snapshot.documents.map { doc ->
                            val restauranteId = doc.id
                            val restauranteNombre = doc.getString("nombreRestaurante")
                                ?: doc.getString("nombre")
                                ?: "Restaurante"

                            // Generar chatId consistente
                            val chatId = if (uid < restauranteId) {
                                "${uid}_$restauranteId"
                            } else {
                                "${restauranteId}_$uid"
                            }

                            Triple(restauranteId, restauranteNombre, chatId)
                        }

                        // Ahora buscar el último mensaje de cada chat
                        val itemsConMensaje = mutableListOf<ChatItemDisplay>()
                        var procesados = 0

                        restaurantes.forEach { (restauranteId, restauranteNombre, chatId) ->
                            // Buscar si existe el chat
                            firestore.collection("chat").document(chatId).get()
                                .addOnSuccessListener { chatDoc ->
                                    val ultimoMensaje = if (chatDoc.exists()) {
                                        chatDoc.getString("ultimoMensaje") ?: ""
                                    } else {
                                        ""
                                    }

                                    itemsConMensaje.add(
                                        ChatItemDisplay(
                                            id = restauranteId,
                                            nombre = restauranteNombre,
                                            ultimoMensaje = ultimoMensaje,
                                            chatId = chatId
                                        )
                                    )

                                    procesados++

                                    // Cuando terminemos de procesar todos, actualizar la lista
                                    if (procesados == restaurantes.size) {
                                        displayList = itemsConMensaje.sortedByDescending {
                                            it.ultimoMensaje.isNotEmpty()
                                        }
                                        println("✅ Total restaurantes con mensajes: ${displayList.size}")
                                        isLoading = false
                                    }
                                }
                                .addOnFailureListener { e ->
                                    println("⚠️ Error buscando chat para $restauranteNombre: ${e.message}")

                                    // Agregar sin último mensaje
                                    itemsConMensaje.add(
                                        ChatItemDisplay(
                                            id = restauranteId,
                                            nombre = restauranteNombre,
                                            ultimoMensaje = "",
                                            chatId = chatId
                                        )
                                    )

                                    procesados++

                                    if (procesados == restaurantes.size) {
                                        displayList = itemsConMensaje
                                        isLoading = false
                                    }
                                }
                        }

                        // Si no hay restaurantes, terminar
                        if (restaurantes.isEmpty()) {
                            displayList = emptyList()
                            isLoading = false
                        }
                    }
                }
        } else {
            // ✅ RESTAURANTE: Mostrar solo chats existentes
            println("📋 Cargando chats del restaurante...")

            firestore.collection("chat")
                .whereArrayContains("participantes", uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        println("❌ Error cargando chats: ${error.message}")
                        displayList = emptyList()
                        isLoading = false
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        displayList = snapshot.documents.mapNotNull { doc ->
                            val data = doc.data ?: return@mapNotNull null
                            val clienteNombre = data["clienteNombre"] as? String ?: "Cliente"
                            val ultimoMensaje = data["ultimoMensaje"] as? String ?: ""

                            println("💬 Chat encontrado: $clienteNombre - $ultimoMensaje")

                            ChatItemDisplay(
                                id = doc.id,
                                nombre = clienteNombre,
                                ultimoMensaje = ultimoMensaje,
                                chatId = doc.id
                            )
                        }

                        println("✅ Total chats cargados: ${displayList.size}")
                        isLoading = false
                    }
                }
        }
    }

    // 🔹 Mostrar loading mientras se determina el rol
    if (userRole == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF641717)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cargando...", color = Color.White)
            }
        }
        return
    }

    Scaffold(containerColor = Color(0xFF641717)) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF641717))
        ) {
            // ===== CÍRCULOS DECORATIVOS =====
            Image(
                painter = painterResource(id = R.drawable.group_3),
                contentDescription = "Círculo izquierdo",
                modifier = Modifier
                    .width(110.dp)
                    .height(250.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.None
            )

            Image(
                painter = painterResource(id = R.drawable.group_5),
                contentDescription = "Círculo derecho",
                modifier = Modifier
                    .width(110.dp)
                    .height(65.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.None
            )

            // ===== PANEL BLANCO =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(720.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // ===== TÍTULO =====
                Text(
                    text = "Chats",
                    style = TextStyle(
                        fontSize = 44.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF863939)
                    )
                )

                Text(
                    text = "Mensajes",
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCB6363)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ===== LÍNEA DIVISORA =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0x54000000))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ===== LISTA DE RESTAURANTES O CHATS =====
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF863939))
                    }
                } else if (displayList.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(displayList) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        println("🔘 Click en: ${item.nombre} (${item.chatId})")
                                        navController.navigate("chat_detail/${item.chatId}")
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
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.nombre.firstOrNull()?.uppercase() ?: "?",
                                        style = TextStyle(
                                            fontSize = 24.sp,
                                            fontFamily = FontFamily(Font(R.font.afacad)),
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Info del chat
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.nombre,
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily(Font(R.font.afacad)),
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF000000)
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    if (item.ultimoMensaje.isNotEmpty()) {
                                        Text(
                                            text = item.ultimoMensaje,
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(Font(R.font.afacad)),
                                                color = Color.Gray
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (userRole == "cliente") {
                                    "No hay restaurantes disponibles"
                                } else {
                                    "No tienes chats todavía"
                                },
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.afacad)),
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Gray
                                )
                            )

                            if (userRole == "restaurante") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Espera a que un cliente te escriba",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.afacad)),
                                        color = Color.Gray
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // ===== ÍCONOS SUPERIORES =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menú",
                    modifier = Modifier.size(32.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Notificaciones",
                    modifier = Modifier.size(32.dp)
                )
            }

            // ===== BOTTOMBAR DINÁMICO SEGÚN EL ROL =====
            if (userRole == "restaurante") {
                BottomBarRestaurante(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    navController = navController,
                    isBackgroundWine = false
                )
            } else {
                BottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    navController = navController,
                    isBackgroundWine = false
                )
            }
        }
    }
}