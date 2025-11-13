package co.edu.karolsaavedra.veezy.ViewRestaurante

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

                    // Obtener cantidades según el tipo
                    if (tipoPedido == "Restaurante") {
                        personas = turnoDoc.getLong("personas")?.toInt()
                    }
                    hamburguesas = turnoDoc.getLong("hamburguesas")?.toInt()
                    papas = turnoDoc.getLong("papas")?.toInt()

                    // Formatear hora
                    val timestamp = turnoDoc.getTimestamp("timestamp")
                    if (timestamp != null) {
                        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                        horaTurno = dateFormat.format(timestamp.toDate())
                    }

                    // Obtener datos del cliente
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
                            // Eliminar el turno de la base de datos
                            db.collection("turnos").document(turnoId)
                                .delete()
                                .addOnSuccessListener {
                                    isDeleting = false
                                    showDialog = false
                                    // Navegar de regreso y limpiar el stack
                                    navController.popBackStack()
                                }
                                .addOnFailureListener { error ->
                                    isDeleting = false
                                    showDialog = false
                                    // Opcional: mostrar mensaje de error
                                    android.util.Log.e("TurnoPendiente", "Error al eliminar turno", error)
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
        containerColor = Color(0xFFFAF0F0),
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAF0F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color(0xFF641717))
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = (-30).dp, y = 80.dp)
                        .border(2.dp, Color(0xFFA979A7), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .offset(x = 250.dp, y = -30.dp)
                        .border(2.dp, Color(0xFFA979A7), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .offset(x = 260.dp, y = -40.dp)
                        .border(3.dp, Color(0xFFA979A7), CircleShape)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 160.dp)
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    )
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = "Turno",
                    color = Color(0xFF641717),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "pendiente",
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

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

                        // Solo mostrar "Personas" si es tipo Restaurante
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
                            // Botón Volver
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

                            // Botón Confirmar
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
            }

            Image(
                painter = painterResource(id = R.drawable.papas),
                contentDescription = "papas",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 60.dp, x = 70.dp),
                contentScale = ContentScale.Crop
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