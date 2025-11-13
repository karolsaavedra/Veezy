package co.edu.karolsaavedra.veezy.ViewRestaurante

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.concurrent.TimeUnit

data class ClienteTurno(
    val turnoId: String,
    val numeroTurno: Long,
    val nombreCliente: String,
    val clienteId: String,
    val tiempoEspera: String,
    val tipo: String,
    val timestamp: com.google.firebase.Timestamp
)

@Composable
fun ClientsWaitingScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val restauranteId = auth.currentUser?.uid

    var nombreRestaurante by remember { mutableStateOf("") }
    var listaClientes by remember { mutableStateOf<List<ClienteTurno>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }
    var mensajeDebug by remember { mutableStateOf("") }

    // PRIMERO: Obtener nombre del restaurante, LUEGO buscar turnos
    LaunchedEffect(restauranteId) {
        if (restauranteId != null) {
            Log.d("ClientsWaiting", "=== PASO 1: Buscando restaurante con ID: $restauranteId")

            db.collection("restaurantes").document(restauranteId).get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        nombreRestaurante = doc.getString("nombreRestaurante") ?: ""
                        Log.d("ClientsWaiting", "=== PASO 2: Nombre del restaurante encontrado: '$nombreRestaurante'")

                        if (nombreRestaurante.isEmpty()) {
                            mensajeDebug = "Campo 'nombreRestaurante' está vacío"
                            cargando = false
                            return@addOnSuccessListener
                        }

                        // AHORA SÍ: Buscar turnos con el nombre del restaurante
                        Log.d("ClientsWaiting", "=== PASO 3: Buscando turnos para: '$nombreRestaurante'")

                        db.collection("turnos")
                            .whereEqualTo("restauranteNombre", nombreRestaurante)
                            .whereEqualTo("estado", "pendiente")
                            .addSnapshotListener { snapshot, error ->
                                if (error != null) {
                                    Log.e("ClientsWaiting", "Error al escuchar turnos", error)
                                    mensajeDebug = "Error: ${error.message}"
                                    cargando = false
                                    return@addSnapshotListener
                                }

                                if (snapshot != null) {
                                    Log.d("ClientsWaiting", "=== PASO 4: Total de turnos encontrados: ${snapshot.size()}")

                                    val turnosTemp = mutableListOf<ClienteTurno>()
                                    var procesados = 0
                                    val totalTurnos = snapshot.documents.size

                                    if (totalTurnos == 0) {
                                        listaClientes = emptyList()
                                        cargando = false
                                        Log.d("ClientsWaiting", "No hay turnos para este restaurante")
                                        return@addSnapshotListener
                                    }

                                    snapshot.documents.forEach { turnoDoc ->
                                        val clienteId = turnoDoc.getString("clienteId") ?: ""
                                        val numeroTurno = turnoDoc.getLong("numero") ?: 0
                                        val tipo = turnoDoc.getString("tipo") ?: "N/A"
                                        val timestamp = turnoDoc.getTimestamp("timestamp") ?: com.google.firebase.Timestamp.now()

                                        Log.d("ClientsWaiting", "Procesando turno #$numeroTurno")

                                        val ahora = Date()
                                        val fechaTurno = timestamp.toDate()
                                        val diferencia = ahora.time - fechaTurno.time
                                        val minutos = TimeUnit.MILLISECONDS.toMinutes(diferencia)
                                        val horas = TimeUnit.MILLISECONDS.toHours(diferencia)

                                        val tiempoEspera = when {
                                            minutos < 60 -> "$minutos min"
                                            horas < 24 -> "$horas hrs"
                                            else -> "${TimeUnit.MILLISECONDS.toDays(diferencia)} días"
                                        }

                                        if (clienteId.isNotEmpty()) {
                                            db.collection("clientes").document(clienteId).get()
                                                .addOnSuccessListener { clienteDoc ->
                                                    val nombre = clienteDoc.getString("nombre") ?: "Usuario"
                                                    val apellido = clienteDoc.getString("apellido") ?: ""
                                                    val nombreCompleto = "$nombre $apellido".trim()

                                                    Log.d("ClientsWaiting", "Cliente encontrado: $nombreCompleto")

                                                    turnosTemp.add(
                                                        ClienteTurno(
                                                            turnoId = turnoDoc.id,
                                                            numeroTurno = numeroTurno,
                                                            nombreCliente = nombreCompleto,
                                                            clienteId = clienteId,
                                                            tiempoEspera = tiempoEspera,
                                                            tipo = tipo,
                                                            timestamp = timestamp
                                                        )
                                                    )

                                                    procesados++
                                                    if (procesados >= totalTurnos) {
                                                        listaClientes = turnosTemp.sortedBy { it.numeroTurno }
                                                        cargando = false
                                                        Log.d("ClientsWaiting", "=== COMPLETADO: ${listaClientes.size} clientes mostrados")
                                                    }
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("ClientsWaiting", "Error al obtener cliente $clienteId", e)
                                                    procesados++
                                                    if (procesados >= totalTurnos) {
                                                        listaClientes = turnosTemp.sortedBy { it.numeroTurno }
                                                        cargando = false
                                                    }
                                                }
                                        } else {
                                            procesados++
                                            if (procesados >= totalTurnos) {
                                                listaClientes = turnosTemp.sortedBy { it.numeroTurno }
                                                cargando = false
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("ClientsWaiting", "Snapshot es null")
                                    cargando = false
                                }
                            }

                    } else {
                        Log.e("ClientsWaiting", "Documento de restaurante no existe")
                        mensajeDebug = "Restaurante no encontrado"
                        cargando = false
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ClientsWaiting", "Error al obtener restaurante", e)
                    mensajeDebug = "Error: ${e.message}"
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
                .background(Color(0xFF641717))
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
                        .offset(x = 250.dp, y = (-30).dp)
                        .border(2.dp, Color(0xFFA979A7), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .offset(x = 260.dp, y = (-40).dp)
                        .border(3.dp, Color(0xFFA979A7), CircleShape)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 160.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = "Clientes",
                    color = Color(0xFF641717),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "en espera",
                    color = Color(0xFF000000),
                    fontSize = 16.sp
                )

                if (mensajeDebug.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Debug: $mensajeDebug",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (cargando) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = Color(0xFF641717))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Cargando clientes...")
                        }
                    }
                } else if (listaClientes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No hay clientes en espera",
                                color = Color(0xFF641717),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (nombreRestaurante.isEmpty())
                                    "Verificando restaurante..."
                                else
                                    "Esperando turnos para: $nombreRestaurante",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(listaClientes) { cliente ->
                            ClientCard(
                                cliente = cliente,
                                navController = navController
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.tocino),
                contentDescription = "tocino",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 80.dp, x = 70.dp),
                contentScale = ContentScale.Fit
            )

            BottomBarRestaurante(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController,
                isBackgroundWine = false
            )
        }
    }
}

// SOLO NECESITAS ACTUALIZAR LA FUNCIÓN ClientCard
// Reemplaza tu función ClientCard actual con esta:

@Composable
fun ClientCard(cliente: ClienteTurno, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cliente.nombreCliente,
                    color = Color(0xFF7F4F4F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = cliente.tipo,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(0.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Turno",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "#${cliente.numeroTurno}",
                            color = Color(0xFF641717),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        Text(
                            text = "Tiempo",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = cliente.tiempoEspera,
                            color = Color(0xFF641717),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        // CAMBIO IMPORTANTE: Pasar el turnoId como parámetro
                        navController.navigate("TurnoPendiente/${cliente.turnoId}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD99C00)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Ver información",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Cliente",
                    tint = Color(0xFF7F4F4F),
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}