package co.edu.karolsaavedra.veezy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    onClickRegister: () -> Unit = {},
    onSuccesfulogin: () -> Unit = {}
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF641717))
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
                    //  Aro superior izquierdo (más grande)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = (-180).dp, y = (-10).dp)
                            .border(
                                width = 3.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    //Aro inferior derecho (pequeño)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .offset(x = (180).dp, y = (160).dp)
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
                            .offset(x = (-170).dp, y = (-5).dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    // Aro inferior derecho (más grande)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = (177).dp, y = (150).dp)
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
                text = "Bienvenido a Veezy",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp), //espacio alrededor
                textAlign = TextAlign.Start, //alineado a la izquierda
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFC64F)
                )
            )
            Text(
                text = "¡No te quedes con \nhambre! Descubre la \nburger ganadora",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp), //espacio alrededor
                textAlign = TextAlign.Start, //alineado a la izquierda
                style = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif,

                    color = Color(0xFFFFFFFF)
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start // 👈 alinea todo a la izquierda
            ) {
                Button(
                    onClick = { onClickRegister() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFAF0F0)
                    ),
                    shape = RoundedCornerShape(12.dp)

                ) {
                    Text(
                        text = "Iniciar sesión",
                        color = Color(0xFF7F4F4F),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp

                    )
                }
                TextButton(onClick = onClickRegister ) {
                    Text(
                        text = "¿No tienes una cuenta? Regístrate",
                        color = Color(0xFFEFB1B1)
                    )
                }

            }







        }
    }
}


