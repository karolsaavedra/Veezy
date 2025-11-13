package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun TurnoScreen(navController: NavController? = null, turnoId: String? = null) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    var numeroTurno by remember { mutableStateOf<Long?>(null) }
    var nombreCliente by remember { mutableStateOf("") }
    var codigoTurno by remember { mutableStateOf("") }
    var nombreRestaurante by remember { mutableStateOf("") }
    var fechaTurno by remember { mutableStateOf("") }
    var estadoTurno by remember { mutableStateOf("") }
    var tiempoTranscurrido by remember { mutableStateOf("") }
    var qrValue by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }
    var turnoDocId by remember { mutableStateOf("") }

    // Cargar datos del usuario
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("clientes").document(userId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        val nombre = doc.getString("nombre") ?: ""
                        val apellido = doc.getString("apellido") ?: ""
                        nombreCliente = "$nombre $apellido"
                    }
                }
        }
    }

    // Cargar último turno del usuario
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("turnos")
                .whereEqualTo("clienteId", userId)
                .whereEqualTo("estado", "pendiente")
                .get()
                .addOnSuccessListener { snapshot ->
                    val ultimoTurno = snapshot.documents.maxByOrNull {
                        it.getTimestamp("timestamp")?.toDate()?.time ?: 0L
                    }

                    if (ultimoTurno != null) {
                        turnoDocId = ultimoTurno.id
                        numeroTurno = ultimoTurno.getLong("numero")
                        nombreRestaurante = ultimoTurno.getString("restauranteNombre") ?: "Desconocido"
                        estadoTurno = when (ultimoTurno.getString("estado")) {
                            "pendiente" -> "En fila"
                            "atendido" -> "Finalizado"
                            "cancelado" -> "Cancelado"
                            else -> "En fila"
                        }

                        // Generar código único (primeras 6 letras del ID del documento)
                        codigoTurno = ultimoTurno.id.take(6).uppercase()

                        // Formatear fecha
                        val timestamp = ultimoTurno.getTimestamp("timestamp")
                        if (timestamp != null) {
                            val date = timestamp.toDate()
                            val dateFormat = SimpleDateFormat("dd/MMM", Locale.getDefault())
                            fechaTurno = dateFormat.format(date).uppercase()

                            // Calcular tiempo transcurrido
                            val ahora = Date()
                            val diferencia = ahora.time - date.time
                            val minutos = TimeUnit.MILLISECONDS.toMinutes(diferencia)
                            val horas = TimeUnit.MILLISECONDS.toHours(diferencia)

                            tiempoTranscurrido = when {
                                minutos < 60 -> "$minutos min"
                                horas < 24 -> "$horas hrs"
                                else -> "${TimeUnit.MILLISECONDS.toDays(diferencia)} días"
                            }
                        }

                        // IMPORTANTE: El QR debe contener SOLO el turnoDocId
                        qrValue = turnoDocId

                        println("=== TURNO GENERADO ===")
                        println("Turno Doc ID: $turnoDocId")
                        println("Número de turno: $numeroTurno")
                        println("QR Value: $qrValue")
                        println("Restaurante: $nombreRestaurante")
                        println("=====================")
                    }
                    cargando = false
                }
                .addOnFailureListener {
                    cargando = false
                }
        }
    }

    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color(0xFF641717))
        ) {
            // Botón cerrar superior
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 40.dp)
                    .align(Alignment.TopStart)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color(0xFFD78D00), shape = CircleShape)
                        .clickable {
                            navController?.navigate("walletCliente") {
                                popUpTo("walletCliente") { inclusive = true }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Cerrar",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Recuadro blanco principal
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.82f)
                    .align(Alignment.Center)
                    .offset(y = 40.dp)
                    .shadow(
                        elevation = 6.dp,
                        spotColor = Color(0x40000000),
                        ambientColor = Color(0x40000000)
                    )
                    .background(color = Color(0xFFFFF9F9), shape = RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                if (cargando) {
                    // Pantalla de carga
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF641717))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando turno...",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color(0xFF641717)
                                )
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Fila de estrellas superior
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            repeat(10) {
                                Image(
                                    painter = painterResource(id = R.drawable.star_1),
                                    contentDescription = "Estrella",
                                    modifier = Modifier.size(24.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Turno",
                            style = TextStyle(
                                fontSize = 36.sp,
                                fontFamily = FontFamily(Font(R.font.afacad)),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF641717),
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = numeroTurno?.toString() ?: "--",
                            style = TextStyle(
                                fontSize = 50.sp,
                                fontFamily = FontFamily(Font(R.font.afacad)),
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Primera fila de información
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            InfoColumn("Día", fechaTurno.ifEmpty { "--" })
                            InfoColumn("Estado", estadoTurno.ifEmpty { "--" })
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Segunda fila de información
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            InfoColumn("Código", codigoTurno.ifEmpty { "--" })
                            InfoColumn("Tiempo", tiempoTranscurrido.ifEmpty { "--" })
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Usuario
                        InfoColumn("Usuario", nombreCliente.ifEmpty { "Cargando..." })

                        Spacer(modifier = Modifier.height(16.dp))

                        // Restaurante
                        Text(
                            text = "Restaurante",
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.afacad)),
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFFB2B2B2),
                                textAlign = TextAlign.Center
                            )
                        )

                        Text(
                            text = nombreRestaurante.ifEmpty { "Cargando..." },
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.afacad)),
                                fontWeight = FontWeight.Normal,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Código QR
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(color = Color(0x96EBA3A3), shape = RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (qrValue.isNotEmpty()) {
                                Barcode(
                                    value = qrValue,
                                    type = BarcodeType.QR_CODE,
                                    modifier = Modifier.size(300.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Fila de estrellas inferior
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            repeat(10) {
                                Image(
                                    painter = painterResource(id = R.drawable.star_1),
                                    contentDescription = "Estrella",
                                    modifier = Modifier.size(24.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoColumn(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.afacad)),
                fontWeight = FontWeight.Normal,
                color = Color(0xFFB2B2B2),
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.afacad)),
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        )
    }
}