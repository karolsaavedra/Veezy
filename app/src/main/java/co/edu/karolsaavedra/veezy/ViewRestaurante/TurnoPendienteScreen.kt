package co.edu.karolsaavedra.veezy.ViewRestaurante

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TurnoPendienteScreen(navController: NavController, turnoId: String) {
    val db = FirebaseFirestore.getInstance()

    var nombreCliente by remember { mutableStateOf("") }
    var apellidoCliente by remember { mutableStateOf("") }
    var emailCliente by remember { mutableStateOf("") }
    var numeroTurno by remember { mutableStateOf<Long?>(null) }
    var tipoPedido by remember { mutableStateOf("") }
    var personas by remember { mutableStateOf<Int?>(null) }
    var hamburguesas by remember { mutableStateOf<Int?>(null) }
    var papas by remember { mutableStateOf<Int?>(null) }
    var horaTurno by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }

    // Obtener información del turno específico
    LaunchedEffect(turnoId) {
        db.collection("turnos").document(turnoId).get()
            .addOnSuccessListener { turnoDoc ->
                if (turnoDoc.exists()) {
                    numeroTurno = turnoDoc.getLong("numero")
                    tipoPedido = turnoDoc.getString("tipo") ?: "N/A"

                    if (tipoPedido == "Restaurante") {
                        personas = turnoDoc.getLong("personas")?.toInt()
                    }
                    hamburguesas = turnoDoc.getLong("hamburguesas")?.toInt()
                    papas = turnoDoc.getLong("papas")?.toInt()

                    val timestamp = turnoDoc.getTimestamp("timestamp")
                    if (timestamp != null) {
                        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                        horaTurno = dateFormat.format(timestamp.toDate())
                    }

                    val clienteId = turnoDoc.getString("clienteId")
                    if (!clienteId.isNullOrEmpty()) {
                        db.collection("clientes").document(clienteId).get()
                            .addOnSuccessListener { clienteDoc ->
                                if (clienteDoc.exists()) {
                                    nombreCliente = clienteDoc.getString("nombre") ?: ""
                                    apellidoCliente = clienteDoc.getString("apellido") ?: ""
                                    emailCliente = clienteDoc.getString("email") ?: ""
                                }
                                isLoading = false
                            }
                            .addOnFailureListener {
                                isLoading = false
                            }
                    } else {
                        isLoading = false
                    }
                } else {
                    isLoading = false
                }
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isDeleting) showDialog = false
            },
            title = {
                Text(
                    text = "Confirmar turno",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF641717)
                )
            },
            text = {
                if (isDeleting) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(color = Color(0xFF641717))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Confirmando turno...")
                    }
                } else {
                    Text("¿Confirmar que el turno #${numeroTurno} ha sido completado? Esta acción eliminará el turno de la lista de espera.")
                }
            },
            confirmButton = {
                if (!isDeleting) {
                    Button(
                        onClick = {
                            isDeleting = true
                            db.collection("turnos").document(turnoId)
                                .delete()
                                .addOnSuccessListener {
                                    isDeleting = false
                                    showDialog = false
                                    navController.popBackStack()
                                }
                                .addOnFailureListener { error ->
                                    isDeleting = false
                                    showDialog = false
                                    Log.e("TurnoPendiente", "Error al eliminar turno", error)
                                }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF641717)
                        )
                    ) {
                        Text("Confirmar", color = Color.White)
                    }
                }
            },
            dismissButton = {
                if (!isDeleting) {
                    OutlinedButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancelar", color = Color(0xFF641717))
                    }
                }
            }
        )
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF641717))
        }
        return
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
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // ===== TÍTULO =====
                Text(
                    text = "Turno",
                    color = Color(0xFF641717),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "pendiente",
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ===== CONTENIDO CON SCROLL =====
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x85D9D9D9), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ID cliente",
                                    color = Color(0xFF7F4F4F),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color.White, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Cliente",
                                        tint = Color(0xFF7F4F4F),
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            InfoRow(label = "Nombre:", value = nombreCliente.ifEmpty { "-" })
                            InfoRow(label = "Apellido:", value = apellidoCliente.ifEmpty { "-" })

                            if (tipoPedido == "Restaurante") {
                                InfoRow(label = "Personas:", value = personas?.toString() ?: "0")
                            }

                            InfoRow(label = "Hamburguesas:", value = hamburguesas?.toString() ?: "0")
                            InfoRow(label = "Papas:", value = papas?.toString() ?: "0")
                            InfoRow(label = "Tipo de pedido:", value = tipoPedido.ifEmpty { "-" })
                            InfoRow(label = "Turno:", value = numeroTurno?.toString() ?: "-")
                            InfoRow(label = "Hora:", value = horaTurno.ifEmpty { "-" })

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { navController.navigateUp() },
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(45.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF641717)
                                    )
                                ) {
                                    Text(
                                        text = "Volver",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Button(
                                    onClick = { showDialog = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFFC64F)
                                    ),
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(45.dp)
                                ) {
                                    Text(
                                        text = "Confirmar",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    // Espacio extra para evitar que la BottomBar tape el contenido
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // ===== IMAGEN DECORATIVA =====
            Image(
                painter = painterResource(id = R.drawable.papas),
                contentDescription = "papas",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 60.dp, x = 70.dp),
                contentScale = ContentScale.Crop
            )

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

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF641717),
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = value,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}