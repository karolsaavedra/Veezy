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
import androidx.compose.ui.draw.shadow
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
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import co.edu.karolsaavedra.veezy.R

@Composable
fun ProductosScreen() {
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

            // ===== PANEL BLANCO PRINCIPAL (más abajo) =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(720.dp)
                    .offset(y = 0.dp) // lo bajamos para que los íconos queden arriba
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // ===== FILA DE TÍTULO Y BOTONES =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Texto “Productos”
                    Text(
                        text = "Productos",
                        style = TextStyle(
                            fontSize = 40.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF863939)
                        )
                    )

                    // Fila con los dos botones (+ y gris)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Botón +
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFD78D00), shape = RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.plus),
                                contentDescription = "Agregar",
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        // Botón gris (lupa + tres puntos)
                        Row(
                            modifier = Modifier
                                .width(85.dp)
                                .height(40.dp)
                                .background(
                                    Color(0xC4D9D9D9),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.search___iconly_pro),
                                contentDescription = "Buscar",
                                modifier = Modifier.size(24.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.more_horizontal),
                                contentDescription = "Opciones",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(90.dp))

                // ===== TARJETAS APILADAS UNA ENCIMA DE OTRA =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // La tarjeta inferior se ve poco (oscura)
                    TurnoCard(
                        color = Color(0xFF641717),
                        modifier = Modifier
                            .offset(y = -60.dp)
                            .shadow(8.dp, RoundedCornerShape(20.dp))
                    )
                    // Tarjeta del medio
                    TurnoCard(
                        color = Color(0xFF863939),
                        modifier = Modifier
                            .offset(y = -30.dp)
                            .shadow(8.dp, RoundedCornerShape(20.dp))
                    )
                    // Tarjeta superior (clara)
                    TurnoCard(
                        color = Color(0xFFCB6363),
                        modifier = Modifier.shadow(8.dp, RoundedCornerShape(20.dp))
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

@Composable
fun TurnoCard(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(382.dp)
            .height(160.dp)
            .background(color, shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Texto
            Text(
                text = "Turno en fila",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.afacad)),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC64F)
                )
            )

            // Cuadro QR
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(Color.White, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.qr_code___iconly_pro),
                    contentDescription = "QR",
                    modifier = Modifier.size(90.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProductosScreen() {
    ProductosScreen()
}
