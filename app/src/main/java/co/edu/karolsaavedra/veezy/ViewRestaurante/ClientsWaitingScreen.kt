package co.edu.karolsaavedra.veezy.ViewRestaurante

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import co.edu.karolsaavedra.veezy.ViewRestaurante.ClientsWaitingScreen


@Composable
fun ClientsWaitingScreen(navController: NavController){ // Agregado correctamente

    Scaffold(
        containerColor = Color(0xFFFAF0F0),
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding() // evita que la barra quede muy abajo
                    .background(Color(0xFF641717))
            ) {
                BottomBarRestaurante(navController = navController, isBackgroundWine = false)
            }
        }
    ){ padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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

            //Contenido principal
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
                    text = "Clientes",
                    color = Color(0xFF641717),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "En espera",
                    color = Color(0xFF000000),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                ClientCard()
                Spacer(modifier = Modifier.height(20.dp))
                ClientCard()
            }

            //Imagen decorativa
            Image(
                painter = painterResource(id = R.drawable.tocino),
                contentDescription = "tocino",
                modifier = Modifier
                    .size(163.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 100.dp, x = 70.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ClientCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(206.dp)
            .background(Color(0xFFF4F4F4), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            //Información del cliente
            Column {
                Text(
                    text = "ID Cliente",
                    color = Color(0xFF7F4F4F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Text(
                        text = "Turno",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tiempo",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = { /* Acción Información */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD99C00)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = "Información",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            // Icono del cliente
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFFFFFF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Cliente",
                    tint = Color(0xFF7F4F4F),
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClientsWaitingScreen() {
    val navController = rememberNavController()
    ClientsWaitingScreen(navController = navController)
}
