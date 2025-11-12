package co.edu.karolsaavedra.veezy.producto

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.menu.BurgerInfo
import co.edu.karolsaavedra.veezy.menu.MenuScreen
import co.edu.karolsaavedra.veezy.menu.burgerList
import com.google.android.gms.maps.model.LatLng


@Composable
fun InfoProducto(navController: NavController) {


    var isFavorite by remember { mutableStateOf(false) }
    var ubicacionRestaurante = LatLng(7.108988715896521, -73.10634914047473)
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF641717))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 54.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { /* acción vacía */ }
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Volver",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { isFavorite = !isFavorite }
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isFavorite) R.drawable.star_yellow else R.drawable.star
                        ),
                        contentDescription = "Favorito",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // --- Contenido principal ---
            BurgerInfo(burger = burgerList[0])
        }

        // --- Botones inferiores ---
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón "Turno"
            Button(
                onClick = { navController.navigate("ReservarTurnoRestaurante") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC00)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .height(55.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Turno",
                    color = Color(0xFF641717),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Botón de ubicación (solo ícono)
            Card(
                modifier = Modifier.size(55.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCC00)),
                onClick = {
                    var url= Uri.parse("google.navigation:q=${ubicacionRestaurante.latitude},${ubicacionRestaurante.longitude}&mode=d")
                    var intent = Intent(Intent.ACTION_VIEW, url)
                    intent.setPackage("com.google.android.apps.maps")
                    context.startActivity(intent)

                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Ubicación",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfopProductopreview() {
    val navController = rememberNavController()
    BurgerInfo(burger = burgerList[0])
    InfoProducto(navController = navController)
}
