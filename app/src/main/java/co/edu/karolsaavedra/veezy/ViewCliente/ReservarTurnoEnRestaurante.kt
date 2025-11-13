package co.edu.karolsaavedra.veezy.ViewCliente

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


@Composable
fun PaginaReservas(
    navController: NavController? = null,
    onClickParaLlevar: () -> Unit = {},
    onClickRestaurante: () -> Unit = {},
    nombreRestaurante: String = "Desconocido",

    ) {
    var personasRest by remember { mutableStateOf(0) }
    var hamburguesasRest by remember { mutableStateOf(0) }
    var papasRest by remember { mutableStateOf(0) }

    var hamburguesasLlevar by remember { mutableStateOf(0) }
    var papasLlevar by remember { mutableStateOf(0) }

    var opcionSeleccionada by remember { mutableStateOf("Restaurante") }
    var turnoAsignado by remember { mutableStateOf<Int?>(null) }
    var generandoTurno by remember { mutableStateOf(false) }


    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFFAF0F0),
        bottomBar = {
            navController?.let {
                BottomBar(navController = it)
            } ?: BottomBarPreview()
        }
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
                    text = "Reserva",
                    color = Color(0xFF641717),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "tu turno ahora!",
                    color = Color(0xFF000000),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = { opcionSeleccionada = "Restaurante" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (opcionSeleccionada == "Restaurante")
                                Color(0xFFFFC64F)
                            else Color(0xFFEFEFEF),
                            contentColor = if (opcionSeleccionada == "Restaurante")
                                Color.White
                            else Color(0xFF641717)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Restaurante", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { opcionSeleccionada = "Para llevar" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (opcionSeleccionada == "Para llevar")
                                Color(0xFFFFC64F)
                            else Color(0xFFEFEFEF),
                            contentColor = if (opcionSeleccionada == "Para llevar")
                                Color.White
                            else Color(0xFF641717)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Para llevar", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val fondoItems = if (opcionSeleccionada == "Restaurante") Color(0xFFFDECEC) else Color(
                    0xFFFDECEC
                )
                if (opcionSeleccionada == "Restaurante") {
                    ItemContador("Personas", personasRest, fondoItems,
                        onSumar = { personasRest++ },
                        onRestar = { if (personasRest > 0) personasRest-- })
                    ItemContador("Hamburguesas", hamburguesasRest, fondoItems,
                        onSumar = { hamburguesasRest++ },
                        onRestar = { if (hamburguesasRest > 0) hamburguesasRest-- })
                    ItemContador("Papas", papasRest, fondoItems,
                        onSumar = { papasRest++ },
                        onRestar = { if (papasRest > 0) papasRest-- })
                } else {
                    ItemContador("Hamburguesas", hamburguesasLlevar, fondoItems,
                        onSumar = { hamburguesasLlevar++ },
                        onRestar = { if (hamburguesasLlevar > 0) hamburguesasLlevar-- })
                    ItemContador("Papas", papasLlevar, fondoItems,
                        onSumar = { papasLlevar++ },
                        onRestar = { if (papasLlevar > 0) papasLlevar-- })
                }
                Spacer(modifier = Modifier.height(80.dp))

                turnoAsignado?.let {
                    Text(
                        text = "Tu turno es: $it",
                        color = Color(0xFF641717),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController?.navigate("InfoProducto") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEFEFEF),
                            contentColor = Color(0xFF641717)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            generandoTurno = true

                            val clienteId = auth.currentUser?.uid
                            if (clienteId == null) {
                                Toast.makeText(context, "Error: usuario no autenticado", Toast.LENGTH_SHORT).show()
                                generandoTurno = false
                                return@Button
                            }

                            val restauranteNombreFinal = nombreRestaurante.ifEmpty { "Desconocido" }

                            // CAMBIO AQUÍ: Primero obtenemos todos los turnos del restaurante y tipo
                            FirebaseFirestore.getInstance().collection("turnos")
                                .whereEqualTo("restauranteNombre", restauranteNombreFinal)
                                .whereEqualTo("tipo", opcionSeleccionada)
                                .get()
                                .addOnSuccessListener { documents ->
                                    // Buscamos el número más alto manualmente
                                    val ultimoNumero = documents.mapNotNull {
                                        it.getLong("numero")
                                    }.maxOrNull() ?: 0

                                    val nuevoTurnoLong = ultimoNumero + 1

                                    val turnoData = hashMapOf(
                                        "numero" to nuevoTurnoLong,
                                        "restauranteNombre" to restauranteNombreFinal,
                                        "tipo" to opcionSeleccionada,
                                        "clienteId" to clienteId,
                                        "timestamp" to com.google.firebase.Timestamp.now(),
                                        "estado" to "pendiente"
                                    )

                                    if (opcionSeleccionada == "Restaurante") {
                                        turnoData["personas"] = personasRest
                                        turnoData["hamburguesas"] = hamburguesasRest
                                        turnoData["papas"] = papasRest
                                    } else {
                                        turnoData["hamburguesas"] = hamburguesasLlevar
                                        turnoData["papas"] = papasLlevar
                                    }

                                    FirebaseFirestore.getInstance().collection("turnos")
                                        .add(turnoData)
                                        .addOnSuccessListener {
                                            Log.d("Turnos", "Turno registrado correctamente: #$nuevoTurnoLong")
                                            turnoAsignado = nuevoTurnoLong.toInt()
                                            generandoTurno = false

                                            Toast.makeText(
                                                context,
                                                "Turno guardado correctamente: #$nuevoTurnoLong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("Turnos", "Error al guardar el turno", e)
                                            generandoTurno = false
                                            Toast.makeText(
                                                context,
                                                "Error al guardar el turno. Intenta nuevamente.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Turnos", "Error al consultar turnos", e)
                                    generandoTurno = false
                                    Toast.makeText(
                                        context,
                                        "Error al generar el turno. Intenta nuevamente.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC64F),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Reservar", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            Image(
                painter = painterResource(id = R.drawable.hamburguesa),
                contentDescription = "Hamburguesa",
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 20.dp, x = 80.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ItemContador(
    titulo: String,
    cantidad: Int,
    fondoColor: Color,
    onSumar: () -> Unit,
    onRestar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = fondoColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x85D9D9D9)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                color = Color(0xFF641717),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 20.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRestar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Restar",
                        tint = Color(0xFF641717)
                    )
                }
                Text(
                    text = cantidad.toString().padStart(2, '0'),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF641717)
                )
                IconButton(onClick = onSumar) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Sumar",
                        tint = Color(0xFF641717)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBarPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text("BottomBar Preview", color = Color.DarkGray)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPaginaReservas() {
    val navController = rememberNavController()
    PaginaReservas(navController = navController)
}