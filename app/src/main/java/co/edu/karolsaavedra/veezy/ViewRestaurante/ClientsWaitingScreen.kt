package co.edu.karolsaavedra.veezy.ViewRestaurante

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante

@Composable
fun ClientsWaitingScreen(navController: NavController) {

    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF641717))
        ) {
            // ===== ENCABEZADO BURDEOS CON DECORACIONES =====
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
                        .offset(x = 250.dp, y = (-30).dp)
                        .border(2.dp, Color(0xFFA979A7), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .offset(x = 260.dp, y = (-40).dp)
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

            // ===== PANEL BLANCO PRINCIPAL =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 160.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
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

                Spacer(modifier = Modifier.height(20.dp))

                ClientCard(navController)
                Spacer(modifier = Modifier.height(20.dp))
                ClientCard(navController)

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ===== IMAGEN DECORATIVA =====
            Image(
                painter = painterResource(id = R.drawable.tocino),
                contentDescription = "tocino",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 80.dp, x = 70.dp),
                contentScale = ContentScale.Fit
            )

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
fun ClientCard(navController: NavController) {
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
                    onClick = {  navController.navigate("TurnoPendiente") },
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