package co.edu.karolsaavedra.veezy

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

@Composable
fun MapaScreen() {
    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF641717))
        ) {
            // ===== CÍRCULOS DECORATIVOS DEL FONDO =====
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
                    .offset(y = 0.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {

                // ===== TÍTULO "Hamburguesas" =====
                Box(
                    modifier = Modifier
                        .width(321.dp)
                        .height(45.dp)
                ) {
                    Text(
                        text = "Hamburguesas",
                        style = TextStyle(
                            fontSize = 48.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFF863939)
                        )
                    )
                }

                // ===== SUBTÍTULO "cerca a ti" =====
                Box(
                    modifier = Modifier
                        .width(318.dp)
                        .height(32.dp)
                ) {
                    Text(
                        text = "cerca a ti",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ===== ÁREA DEL MAPA =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .background(
                            color = Color(0xFFE5E5E5),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Texto placeholder del mapa (puedes reemplazarlo con un mapa real)
                    Text(
                        text = "Mapa de los\nrestaurante",
                        style = TextStyle(
                            fontSize = 48.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFCB6363).copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.offset(x = 0.dp, y = 0.dp)
                    )
                }
            }

            // ===== ÍCONOS DE MENÚ Y CAMPANITA (sobre fondo rojizo) =====
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

            // ===== BARRA INFERIOR =====
            BottomBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMapaScreen() {
    MapaScreen()
}