package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R

@Composable
fun ChatClienteScreen() {
    var mensajes by remember { mutableStateOf(listOf("Hola, ¿cómo puedo ayudarte?")) }
    var nuevoMensaje by remember { mutableStateOf(TextFieldValue("")) }

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
                contentDescription = null,
                modifier = Modifier
                    .width(110.dp)
                    .height(250.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.None
            )
            Image(
                painter = painterResource(id = R.drawable.group_5),
                contentDescription = null,
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
                // ===== HEADER CON PERFIL =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Círculo de perfil
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFFFFC64F), shape = RoundedCornerShape(30.dp))
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Texto “Restaurante”
                        Text(
                            text = "Restaurante",
                            style = TextStyle(
                                fontSize = 36.sp,
                                fontFamily = FontFamily(Font(R.font.afacad)),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF863939)
                            )
                        )
                    }

                    // Icono de llamada
                    Image(
                        painter = painterResource(id = R.drawable.call),
                        contentDescription = "Llamar",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(1.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // ===== LÍNEA DIVISORA =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0x54000000))
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ===== LISTA DE MENSAJES =====
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(mensajes) { mensaje ->
                        if (mensaje.startsWith("Yo:")) {
                            // Mensaje propio
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(261.dp)
                                        .background(
                                            color = Color(0xFFE08A8A),
                                            shape = RoundedCornerShape(
                                                topStart = 20.dp,
                                                topEnd = 20.dp,
                                                bottomStart = 20.dp,
                                                bottomEnd = 0.dp
                                            )
                                        )
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = mensaje.removePrefix("Yo: "),
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.afacad)),
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        } else {
                            // Mensaje recibido
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(261.dp)
                                        .background(
                                            color = Color(0xFFD9D9D9),
                                            shape = RoundedCornerShape(
                                                topStart = 20.dp,
                                                topEnd = 20.dp,
                                                bottomStart = 0.dp,
                                                bottomEnd = 20.dp
                                            )
                                        )
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = mensaje,
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.afacad)),
                                            color = Color.Black
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ===== FLECHA SUPERIOR (volver) =====
            Image(
                painter = painterResource(id = R.drawable.arrow___left_2___iconly_pro),
                contentDescription = "Volver",
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.Fit
            )

            // ===== BARRA DE MENSAJE INFERIOR =====
            Box(
                modifier = Modifier
                    .width(412.dp)
                    .height(93.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color(0xFF641717),
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Campo de texto
                    Box(
                        modifier = Modifier
                            .width(310.dp)
                            .height(47.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x85D9D9D9)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = nuevoMensaje,
                            onValueChange = { nuevoMensaje = it },
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.afacad)),
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth()
                        )
                    }

                    // Icono de enviar (funcional)
                    Image(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = "Enviar mensaje",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 5.dp)
                            .clickable {
                                val texto = nuevoMensaje.text.trim()
                                if (texto.isNotEmpty()) {
                                    mensajes = mensajes + "Yo: $texto"
                                    nuevoMensaje = TextFieldValue("")
                                }
                            },
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatClienteScreen() {
    ChatClienteScreen()
}
