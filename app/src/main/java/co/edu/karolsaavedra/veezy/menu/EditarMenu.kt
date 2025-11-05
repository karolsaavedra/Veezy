package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar2

@Preview(showBackground = true)
@Composable
fun EditarMenuScreen() {
    Scaffold(
        containerColor = Color.White // Fondo general blanco
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ===== ZONA ROJA CON BORDES REDONDEADOS =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.20f)
                    .background(
                        color = Color(0xFF641717),
                        shape = RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp)
                    )
            )

            Image(
                painter = painterResource(id = R.drawable.group_3),
                contentDescription = "Círculo superior izquierdo",
                modifier = Modifier
                    .width(110.dp)
                    .height(200.dp)
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
            // ===== TÍTULO =====
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp)
            ) {
                Text(
                    text = "Editar Menú",
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }


            // ===== CARD DEL FORMULARIO =====
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(470.dp)
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3F3)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Imagen superior
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color(0xFFE5E5E5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Imagen del producto",
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Campos de texto
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        LabeledTextField(
                            label = "Nombre:",
                            placeholder = "Nombre",
                            labelColor = Color(0xFF641717)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LabeledTextField(
                            label = "Horario de atención:",
                            placeholder = "Horario de atención",
                            labelColor = Color(0xFF641717)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LabeledTextField(
                            label = "Precio:",
                            placeholder = "Valor",
                            labelColor = Color(0xFFD4A017)
                        )
                    }
                }
            }

            // ===== BOTÓN SIGUIENTE =====
            Button(
                onClick = { " " },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC00)),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
                    .width(180.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Siguiente",
                    color = Color(0xFF641717),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // ===== BARRA INFERIOR =====
            BottomBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun LabeledTextField(label: String, placeholder: String, labelColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = labelColor,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(placeholder, color = Color(0xFFBDBDBD)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
