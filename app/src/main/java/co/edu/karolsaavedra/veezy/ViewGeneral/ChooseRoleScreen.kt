package co.edu.karolsaavedra.veezy.ViewGeneral

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R

@Preview
@Composable
fun ChooseRoleScreen(
    onClickCliente: () -> Unit = {},
    onSuccesfulcliente: () -> Unit = {},
    onClickRestaurante: () -> Unit = {},
    onSuccesfulrestaurante: () -> Unit = {}) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF0F0))
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                Modifier
                    .width(356.dp)
                    .height(356.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Aro superior derecho (grande)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = 177.dp, y = (-10).dp)
                            .border(
                                width = 3.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    // Aro superior derecho (pequeño)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .offset(x = 183.dp, y = (-20).dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    // Aro inferior izquierdo (pequeño)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .offset(x = (-170).dp, y = 160.dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    // Aro inferior izquierdo (grande)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = (-180).dp, y = 150.dp)
                            .border(
                                width = 3.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo_master),
                        contentDescription = "Logo Burger Master",
                        modifier = Modifier.size(400.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Iniciar sesión\ncomo",
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp), //espacio alrededor
                textAlign = TextAlign.Start, //alineado a la izquierda
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,

                    color = Color(0xFF863939)
                )
            )


            Spacer(modifier = Modifier.height(40.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start //alineado a la izquierda
            ) {
                Button(
                    onClick = { onClickCliente() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF863939)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = "Cliente",
                        color = Color(0xFFFFFFFF), // texto dorado
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Button(
                    onClick = { onClickCliente() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF863939)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = "Restaurante",
                        color = Color(0xFFFFFFFF), // texto dorado
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}