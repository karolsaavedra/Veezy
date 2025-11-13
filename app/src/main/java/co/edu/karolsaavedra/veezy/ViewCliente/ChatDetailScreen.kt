package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.google.firebase.firestore.Query

data class Mensaje(
    val texto: String = "",
    val emisorId: String = "",
    val timestamp: Any? = null
)

@Composable
fun ChatDetailScreen(navController: NavController, chatId: String) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    var mensajes by remember { mutableStateOf<List<Mensaje>>(emptyList()) }
    var nuevoMensaje by remember { mutableStateOf("") }
    var nombreDestino by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // 🔹 Inicializar chat (detectando automáticamente quién es cliente/restaurante)
    LaunchedEffect(chatId) {
        if (currentUser == null) {
            errorMessage = "Usuario no autenticado"
            isLoading = false
            return@LaunchedEffect
        }

        println("🔍 chatId: $chatId, Usuario: ${currentUser.uid}")

        val chatRef = firestore.collection("chat").document(chatId)

        chatRef.get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    // Chat existe
                    val data = doc.data
                    if (data != null) {
                        println("📦 Chat existe con datos: $data")
                        val esCliente = data["clienteId"] == currentUser.uid
                        nombreDestino = if (esCliente) {
                            data["restauranteNombre"] as? String ?: "Restaurante"
                        } else {
                            data["clienteNombre"] as? String ?: "Cliente"
                        }
                        println("✅ Nombre cargado: $nombreDestino")
                    }
                    isLoading = false
                } else {
                    println("⚠️ Chat no existe, creando...")

                    // Chat no existe, crear
                    val ids = chatId.split("_")
                    if (ids.size != 2) {
                        errorMessage = "ID de chat inválido"
                        isLoading = false
                        return@addOnSuccessListener
                    }

                    // 🔧 DETECTAR AUTOMÁTICAMENTE QUIÉN ES QUIÉN
                    firestore.collection("clientes").document(ids[0]).get()
                        .addOnSuccessListener { doc0 ->
                            val clienteId: String
                            val restauranteId: String

                            if (doc0.exists()) {
                                // ids[0] es cliente
                                clienteId = ids[0]
                                restauranteId = ids[1]
                                println("🔍 ids[0] es cliente")
                            } else {
                                // ids[1] es cliente (ids[0] es restaurante)
                                clienteId = ids[1]
                                restauranteId = ids[0]
                                println("🔍 ids[1] es cliente")
                            }

                            println("👥 Cliente: $clienteId, Restaurante: $restauranteId")

                            // Obtener nombres
                            firestore.collection("clientes").document(clienteId).get()
                                .addOnSuccessListener { clienteDoc ->
                                    firestore.collection("restaurantes").document(restauranteId).get()
                                        .addOnSuccessListener { restauranteDoc ->

                                            val clienteNombre = clienteDoc.getString("nombre") ?: "Cliente"

                                            // 🔧 Buscar nombre del restaurante (puede estar en nombreRestaurante o nombre)
                                            val restauranteNombre = restauranteDoc.getString("nombreRestaurante")
                                                ?: restauranteDoc.getString("nombre")
                                                ?: "Restaurante"

                                            println("📝 Nombres - Cliente: $clienteNombre, Restaurante: $restauranteNombre")

                                            val nuevoChat = hashMapOf(
                                                "chatId" to chatId,
                                                "clienteId" to clienteId,
                                                "restauranteId" to restauranteId,
                                                "clienteNombre" to clienteNombre,
                                                "restauranteNombre" to restauranteNombre,
                                                "ultimoMensaje" to "",
                                                "timestamp" to FieldValue.serverTimestamp(),
                                                "participantes" to listOf(clienteId, restauranteId)
                                            )

                                            chatRef.set(nuevoChat)
                                                .addOnSuccessListener {
                                                    nombreDestino = if (currentUser.uid == clienteId) {
                                                        restauranteNombre
                                                    } else {
                                                        clienteNombre
                                                    }
                                                    println("✅ Chat creado. Nombre: $nombreDestino")
                                                    isLoading = false
                                                }
                                                .addOnFailureListener { e ->
                                                    errorMessage = "Error creando chat: ${e.message}"
                                                    println("❌ Error al crear chat: ${e.message}")
                                                    isLoading = false
                                                }
                                        }
                                        .addOnFailureListener { e ->
                                            errorMessage = "Error cargando restaurante: ${e.message}"
                                            println("❌ Error restaurante: ${e.message}")
                                            isLoading = false
                                        }
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = "Error cargando cliente: ${e.message}"
                                    println("❌ Error cliente: ${e.message}")
                                    isLoading = false
                                }
                        }
                        .addOnFailureListener { e ->
                            errorMessage = "Error detectando roles: ${e.message}"
                            println("❌ Error detectando roles: ${e.message}")
                            isLoading = false
                        }
                }
            }
            .addOnFailureListener { e ->
                errorMessage = "Error: ${e.message}"
                println("❌ Error general: ${e.message}")
                isLoading = false
            }
    }

    // 🔹 Escuchar mensajes
    DisposableEffect(chatId) {
        val chatRef = firestore.collection("chat").document(chatId)

        val listener = chatRef
            .collection("mensajes")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("❌ Error mensajes: ${error.message}")
                    return@addSnapshotListener
                }

                mensajes = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Mensaje(
                            texto = doc.getString("texto") ?: "",
                            emisorId = doc.getString("emisorId") ?: "",
                            timestamp = doc.get("timestamp")
                        )
                    } catch (e: Exception) {
                        println("❌ Error parseando mensaje: ${e.message}")
                        null
                    }
                } ?: emptyList()

                println("✅ ${mensajes.size} mensajes cargados")
            }

        onDispose {
            listener.remove()
        }
    }

    // 🔹 Función enviar
    fun enviarMensaje() {
        val texto = nuevoMensaje.trim()
        if (texto.isEmpty() || currentUser == null) {
            println("❌ No se puede enviar: texto='$texto', user=${currentUser?.uid}")
            return
        }

        val mensaje = hashMapOf(
            "texto" to texto,
            "emisorId" to currentUser.uid,
            "timestamp" to FieldValue.serverTimestamp()
        )

        println("📤 Enviando: $texto")

        firestore.collection("chat").document(chatId)
            .collection("mensajes")
            .add(mensaje)
            .addOnSuccessListener {
                println("✅ Mensaje enviado con ID: ${it.id}")

                // Actualizar último mensaje
                firestore.collection("chat").document(chatId)
                    .update("ultimoMensaje", texto, "timestamp", FieldValue.serverTimestamp())
                    .addOnSuccessListener {
                        println("✅ Último mensaje actualizado")
                    }
                    .addOnFailureListener { e ->
                        println("⚠️ Error actualizando último mensaje: ${e.message}")
                    }

                nuevoMensaje = ""
            }
            .addOnFailureListener { e ->
                println("❌ Error enviando mensaje: ${e.message}")
                errorMessage = "Error enviando: ${e.message}"
            }
    }

    // 🎨 UI
    Scaffold(containerColor = Color(0xFF641717)) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF641717))
        ) {
            Image(
                painter = painterResource(id = R.drawable.group_3),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(250.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.None
            )
            Image(
                painter = painterResource(id = R.drawable.group_5),
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(65.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.None
            )

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
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFFFFC64F), shape = RoundedCornerShape(30.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = nombreDestino.firstOrNull()?.uppercase() ?: "?",
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = if (isLoading) "Cargando..." else nombreDestino.ifEmpty { "Error" },
                                style = TextStyle(
                                    fontSize = 28.sp,
                                    fontFamily = FontFamily(Font(R.font.afacad)),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF863939)
                                )
                            )
                            if (errorMessage.isNotEmpty()) {
                                Text(
                                    text = errorMessage,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        color = Color.Red
                                    )
                                )
                            }
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.call),
                        contentDescription = "Llamar",
                        modifier = Modifier.size(35.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0x54000000))
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Lista mensajes
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(mensajes) { mensaje ->
                        val esMio = mensaje.emisorId == currentUser?.uid

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = if (esMio) Alignment.CenterEnd else Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .widthIn(max = 261.dp)
                                    .background(
                                        color = if (esMio) Color(0xFFE08A8A) else Color(0xFFD9D9D9),
                                        shape = RoundedCornerShape(
                                            topStart = 20.dp,
                                            topEnd = 20.dp,
                                            bottomStart = if (esMio) 20.dp else 0.dp,
                                            bottomEnd = if (esMio) 0.dp else 20.dp
                                        )
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = mensaje.texto,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(Font(R.font.afacad)),
                                        color = if (esMio) Color.White else Color.Black
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.arrow___left_2___iconly_pro),
                contentDescription = "Volver",
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .clickable { navController.popBackStack() },
                contentScale = ContentScale.Fit
            )

            // Input de mensaje
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(93.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color(0xFF641717),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(47.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x85D9D9D9)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = nuevoMensaje,
                            onValueChange = { nuevoMensaje = it },
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.afacad)),
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (nuevoMensaje.isEmpty()) {
                                    Text(
                                        "Escribe un mensaje...",
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.afacad)),
                                            color = Color.Gray
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = "Enviar mensaje",
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                println("🔘 Click enviar")
                                enviarMensaje()
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}