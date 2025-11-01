package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar




@Preview(showBackground = true)
@Composable
fun PaginaReservas( onClickParaLlevar: () -> Unit = {}, onClickRestaurante: () -> Unit = {}) {
    var personas by remember { mutableStateOf(0) }
    var hamburguesas by remember { mutableStateOf(0) }
    var papas by remember { mutableStateOf(0) }
    var opcionSeleccionada by remember { mutableStateOf("Restaurante") }

    Scaffold(
        containerColor = Color(0xFFFAF0F0),
        bottomBar = { BottomBar() }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ← Aplicamos el padding del Scaffold
                .background(Color(0xFFFAF0F0))
        ) {
            // 🔹 Encabezado burdeos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(Color(0xFF641717))
            ) {
                // Aros decorativos
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

                // Iconos superiores
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

                // 🔹 Selector de tipo de pedido
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { opcionSeleccionada = "Restaurante" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (opcionSeleccionada == "Restaurante") Color(0xFFFFC64F) else Color(0xFFEFEFEF),
                            contentColor = if (opcionSeleccionada == "Restaurante") Color.White else Color(0xFF641717)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(
                            text = "Restaurante",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { opcionSeleccionada = "Para llevar"
                                onClickParaLlevar()
                                  },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (opcionSeleccionada == "Para llevar") Color(0xFFE5A900) else Color(0xFFEFEFEF),
                            contentColor = if (opcionSeleccionada == "Para llevar") Color.White else Color(0xFF641717)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(
                            text = "Para llevar",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🔹 Color dinámico según tipo
                val fondoItems = if (opcionSeleccionada == "Restaurante") Color(0xFFFDECEC) else Color(0xFFFFF6E0)

                // 🔹 Lista de productos
                ItemContador("Personas", personas, fondoItems,
                    onSumar = { personas++ },
                    onRestar = { if (personas > 0) personas-- }
                )
                ItemContador("Hamburguesas", hamburguesas, fondoItems,
                    onSumar = { hamburguesas++ },
                    onRestar = { if (hamburguesas > 0) hamburguesas-- }
                )
                ItemContador("Papas", papas, fondoItems,
                    onSumar = { papas++ },
                    onRestar = { if (papas > 0) papas-- }
                )

                Spacer(modifier = Modifier.height(170.dp))

                // 🔹 Botones inferiores
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /*Cancelar*/ },
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
                        onClick = { /*Reservar*/ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC64F),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Reservar turno", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            // 🔹 Imagen hamburguesa sobrepuesta
            Image(
                painter = painterResource(id = R.drawable.hamburguesa),
                contentDescription = "Hamburguesa",
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 20.dp, x = (80).dp),
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
            verticalAlignment = Alignment.CenterVertically,

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


