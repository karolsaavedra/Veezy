package co.edu.karolsaavedra.veezy.ViewRestaurante

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QRScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val restauranteId = auth.currentUser?.uid

    var scanResult by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            if (result.contents != null) {
                scanResult = result.contents
                isProcessing = true

                // El QR contiene SOLO el turnoId (ID del documento)
                val turnoId = result.contents.trim()

                println("=== ESCANEO QR ===")
                println("QR Escaneado: $turnoId")
                println("Longitud QR: ${turnoId.length}")
                println("Restaurante ID actual: $restauranteId")
                println("===================")

                // Buscar el turno en Firebase
                db.collection("turnos").document(turnoId)
                    .get()
                    .addOnSuccessListener { turnoDoc ->
                        println("Documento encontrado: ${turnoDoc.exists()}")

                        if (turnoDoc.exists()) {
                            val turnoRestauranteId = turnoDoc.getString("restauranteId")
                            val turnoRestauranteNombre = turnoDoc.getString("restauranteNombre")
                            val estado = turnoDoc.getString("estado") ?: "pendiente"

                            println("Turno Restaurante ID: $turnoRestauranteId")
                            println("Turno Restaurante Nombre: $turnoRestauranteNombre")
                            println("Estado del turno: $estado")
                            println("Todos los datos del turno: ${turnoDoc.data}")

                            // Si el turno tiene restauranteNombre en lugar de restauranteId
                            // buscar por nombre del restaurante
                            if (turnoRestauranteId == null && turnoRestauranteNombre != null) {
                                // Buscar el restaurante actual por su ID para obtener el nombre
                                db.collection("restaurantes").document(restauranteId ?: "")
                                    .get()
                                    .addOnSuccessListener { restDoc ->
                                        val miNombreRestaurante = restDoc.getString("nombreRestaurante")
                                            ?: restDoc.getString("nombre")

                                        println("Mi nombre de restaurante: $miNombreRestaurante")

                                        if (miNombreRestaurante == turnoRestauranteNombre) {
                                            if (estado == "pendiente" || estado == "activo") {
                                                isProcessing = false
                                                navController.navigate("TurnoPendiente/$turnoId")
                                            } else {
                                                isProcessing = false
                                                errorMessage = "Este turno ya fue completado (Estado: $estado)"
                                                showErrorDialog = true
                                            }
                                        } else {
                                            isProcessing = false
                                            errorMessage = "Este turno es para: $turnoRestauranteNombre\nTu restaurante: $miNombreRestaurante"
                                            showErrorDialog = true
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        isProcessing = false
                                        errorMessage = "Error al verificar restaurante: ${e.message}"
                                        showErrorDialog = true
                                    }
                            } else if (turnoRestauranteId == restauranteId) {
                                // Verificación normal por ID
                                if (estado == "pendiente" || estado == "activo") {
                                    isProcessing = false
                                    navController.navigate("TurnoPendiente/$turnoId")
                                } else {
                                    isProcessing = false
                                    errorMessage = "Este turno ya fue completado (Estado: $estado)"
                                    showErrorDialog = true
                                }
                            } else {
                                isProcessing = false
                                errorMessage = "Este turno no pertenece a tu restaurante\nTurno ID Restaurante: $turnoRestauranteId\nTu ID: $restauranteId"
                                showErrorDialog = true
                            }
                        } else {
                            isProcessing = false
                            errorMessage = "Turno no encontrado.\nQR escaneado: $turnoId"
                            showErrorDialog = true
                        }
                    }
                    .addOnFailureListener { e ->
                        isProcessing = false
                        println("Error al leer turno: ${e.message}")
                        e.printStackTrace()
                        errorMessage = "Error al verificar el turno:\n${e.message}\n\nTurno ID: $turnoId"
                        showErrorDialog = true
                    }
            } else {
                Toast.makeText(context, "No se escaneó ningún código", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Diálogo de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "Error de escaneo",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF641717)
                )
            },
            text = {
                Text(errorMessage)
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF641717)
                    )
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
        )
    }

    // Diálogo de carga
    if (isProcessing) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "Procesando...",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF641717)
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator(color = Color(0xFF641717))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Verificando código QR...")
                }
            },
            confirmButton = { }
        )
    }

    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF641717))
        ) {
            // ===== CÍRCULOS DECORATIVOS =====
            Image(
                painter = painterResource(id = R.drawable.group_3),
                contentDescription = "Círculo superior izquierdo",
                modifier = Modifier
                    .width(110.dp)
                    .height(250.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.None
            )

            Image(
                painter = painterResource(id = R.drawable.group_5),
                contentDescription = "Círculo superior derecho",
                modifier = Modifier
                    .width(110.dp)
                    .height(65.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.None
            )

            // ===== PANEL BLANCO PRINCIPAL =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(720.dp)
                    .offset(y = 0.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                // ===== TÍTULO =====
                Text(
                    text = "QR",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF641717)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Escanea el código QR del cliente",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        color = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                // ===== CUADRO DEL CÓDIGO QR =====
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEFEFEF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code___iconly_pro),
                        contentDescription = "QR Icon",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                // ===== BOTÓN ESCANEAR =====
                Button(
                    onClick = {
                        scanLauncher.launch(
                            ScanOptions().apply {
                                setPrompt("Escanea el código QR del turno")
                                setBeepEnabled(true)
                                setOrientationLocked(true)
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                    modifier = Modifier
                        .width(180.dp)
                        .height(55.dp),
                    enabled = !isProcessing
                ) {
                    Text(
                        text = "Escanear",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }

            // ===== ÍCONOS DE MENÚ (ARRIBA) =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow___left_2___iconly_pro),
                        contentDescription = "Volver",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // ===== BARRA INFERIOR =====
            BottomBarRestaurante(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController,
                isBackgroundWine = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQRScreen() {
    val navController = rememberNavController()
    QRScreen(navController = navController)
}