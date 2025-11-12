package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.annotation.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType

@Composable
fun TurnoScreen(navController: NavController? = null) {
    // NUEVO: Instancias de Firebase
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    // NUEVO: Estados para mostrar los datos en pantalla
    var turno by remember { mutableStateOf<Int?>(null) }
    var nombreCliente by remember { mutableStateOf("") }
    var codigoCliente by remember { mutableStateOf("") }
    var nombreRestaurante by remember { mutableStateOf("") }

    // NUEVO: Cargar información del cliente y su turno
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        nombreCliente =
                            (doc.getString("nombre") ?: "") + " " + (doc.getString("apellido") ?: "")
                        codigoCliente = userId.take(6).uppercase()
                        turno = (doc.getLong("turno") ?: 0L).toInt()

                        // Obtener restaurante del último turno del cliente
                        db.collection("turnos")
                            .whereEqualTo("clienteId", userId)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                val ultimoTurno = snapshot.documents.maxByOrNull {
                                    it.getTimestamp("timestamp")?.toDate()?.time ?: 0L
                                }
                                nombreRestaurante =
                                    ultimoTurno?.getString("restauranteId") ?: "No asignado"
                            }
                    }
                }
        }
    }

    val cadena = "Turno Cliente"
    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color(0xFF641717))
        ) {
            // ===== BOTONES SUPERIORES =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 40.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón amarillo (X)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color(0xFFD78D00), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Cerrar",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Botón gris con íconos
                Row(
                    modifier = Modifier
                        .width(85.dp)
                        .height(40.dp)
                        .background(color = Color(0xC4D9D9D9), shape = RoundedCornerShape(20.dp)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.danger___iconly_pro),
                        contentDescription = "Peligro",
                        modifier = Modifier.size(22.dp),
                        contentScale = ContentScale.Fit
                    )
                    Image(
                        painter = painterResource(id = R.drawable.more_horizontal),
                        contentDescription = "Más opciones",
                        modifier = Modifier.size(22.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // ===== RECUADRO BLANCO PRINCIPAL =====
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.82f)
                    .align(Alignment.Center)
                    .offset(y = 40.dp) // desplazamiento real hacia abajo
                    .shadow(
                        elevation = 6.dp,
                        spotColor = Color(0x40000000),
                        ambientColor = Color(0x40000000)
                    )
                    .background(color = Color(0xFFFFF9F9), shape = RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ===== FILA DE ESTRELLAS SUPERIORES =====
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

                    // ===== TÍTULO "TURNO" =====
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

                    // ===== NÚMERO DEL TURNO DINÁMICO =====
                    Text(
                        text = turno?.toString() ?: "--", //NUEVO: número real del turno
                        style = TextStyle(
                            fontSize = 90.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== FILA DÍA Y ESTADO =====
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoColumn("Día", "15/OCT")
                        InfoColumn("Estado", "En fila")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== FILA CÓDIGO Y TIEMPO =====
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoColumn("Código", "FRC175")
                        InfoColumn("Tiempo", "35 min")
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // ===== USUARIO =====
                    InfoColumn("Usuario", nombreCliente.ifEmpty { "--" })

                    Spacer(modifier = Modifier.height(16.dp))

                    // ===== RESTAURANTE =====
                    Text(
                        text = "Restaurante",
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    )
                    // ===== Nombre restaurante =====
                    Text(
                        text = nombreRestaurante.ifEmpty { "Cargando..." },
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    )



                    Spacer(modifier = Modifier.height(20.dp))

                    // ===== CÓDIGO QR =====
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(color = Color(0x96EBA3A3), shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Barcode(value = cadena, type = BarcodeType.QR_CODE, modifier = Modifier.size(200.dp))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ===== FILA DE ESTRELLAS INFERIORES =====
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

@Preview(showBackground = true)
@Composable
fun PreviewTurnoScreen() {
    TurnoScreen()
}
