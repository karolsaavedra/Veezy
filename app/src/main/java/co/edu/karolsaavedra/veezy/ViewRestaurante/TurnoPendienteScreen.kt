package co.edu.karolsaavedra.veezy.ViewRestaurante

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar

@Composable
fun TurnoPendienteScreen(navController: NavController) { // Se agrega navController
    Scaffold(
        containerColor = Color(0xFFFAF0F0),
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFAF0F0))
        ) {
            //  Encabezado burdeos
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

                // Icono superior
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

            //  Contenido principal
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
                    text = "pendientes",
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 🔹 Card con los datos
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF4F4F4), RoundedCornerShape(16.dp))
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

                        InfoRow(label = "Nombre:", value = "Karol Saavedra")
                        InfoRow(label = "Cantidad:", value = "02")
                        InfoRow(label = "Tipo de pedido:", value = "Para llevar")
                        InfoRow(label = "Turno:", value = "11")
                        InfoRow(label = "Hora:", value = "6 PM")

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { /* Acción confirmar */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC64F)
                            ),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(150.dp)
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

            // Imagen de papas en la parte superior
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
                .align(Alignment.CenterVertically)
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

// ✅ Preview con NavController (idéntico formato al de los otros archivos)
@Preview(showBackground = true)
@Composable
fun PreviewTurnoPendienteScreen() {
    val navController = rememberNavController()
    TurnoPendienteScreen(navController = navController)
}
