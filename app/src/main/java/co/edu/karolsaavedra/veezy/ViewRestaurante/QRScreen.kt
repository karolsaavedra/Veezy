package co.edu.karolsaavedra.veezy.ViewRestaurante

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QRScreen(navController: NavController) {
    var scanResult by remember { mutableStateOf<String?>(null) }
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result -> scanResult = result.contents }
    )

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
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))


                // ===== TÍTULO =====
                Text(
                    text = "QR",
                    style = TextStyle(
                        fontSize = 36.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF641717)
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                // ===== CUADRO DEL CÓDIGO QR =====
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEFEFEF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code___iconly_pro), // tu ícono personalizado
                        contentDescription = "QR Icon",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                // ===== BOTÓN CONFIRMAR =====
                Button(
                    onClick = { scanLauncher.launch(ScanOptions()) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                    modifier = Modifier
                        .width(180.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = "Confirmar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
            }

            // ===== ÍCONOS DE MENÚ (ARRIBA) =====
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
            }

            // ===== BARRA INFERIOR =====
            BottomBarRestaurante(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController,
                isBackgroundWine = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewQRScreen() {
    val navController = rememberNavController()
    QRScreen(navController = navController)
}