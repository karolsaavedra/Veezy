package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar

@Composable
fun ChatScreen(navController: NavController) {
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
                contentDescription = "Círculo izquierdo",
                modifier = Modifier
                    .width(110.dp)
                    .height(250.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.None
            )

            Image(
                painter = painterResource(id = R.drawable.group_5),
                contentDescription = "Círculo derecho",
                modifier = Modifier
                    .width(110.dp)
                    .height(65.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.None
            )

            // ===== PANEL BLANCO =====
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
                // ===== TÍTULO "Chats" Y SUBTÍTULO =====
                Text(
                    text = "Chats",
                    modifier = Modifier
                        .width(260.dp)
                        .height(45.dp),
                    style = TextStyle(
                        fontSize = 44.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF863939)
                    )
                )

                Text(
                    text = "Mensajes",
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(103.dp)
                        .height(20.dp),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCB6363)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ===== LÍNEA DIVISORA =====
                Box(
                    modifier = Modifier
                        .width(371.dp)
                        .height(1.dp)
                        .background(color = Color(0x54000000))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ===== LISTA DE CHATS =====
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    repeat(7) { index ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Esfera amarilla (perfil)
                            Box(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .width(60.dp)
                                    .height(60.dp)
                                    .background(
                                        color = Color(0xFFFFC64F),
                                        shape = RoundedCornerShape(30.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // Nombre del cliente (en lugar de recuadro gris)
                            Text(
                                text = "Cliente ${index + 1}",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.afacad)),
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF000000)
                                )
                            )
                        }
                    }
                }
            }

            // ===== ÍCONOS SUPERIORES =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menú",
                    modifier = Modifier.size(32.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Notificaciones",
                    modifier = Modifier.size(32.dp)
                )
            }

            BottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController, isBackgroundWine = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    ChatScreen(navController = androidx.navigation.compose.rememberNavController())
}