package co.edu.karolsaavedra.veezy.menu

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R

@Preview(showBackground = true)
@Composable
fun AgregarProductoScreen() {
    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF641717))
        ) {

            // ===== CONTENIDO PRINCIPAL =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                IconButton(
                    onClick = { " "},
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Volver",
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                // ===== CONTENEDOR DE IMAGEN =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color(0xFF8C3A3A), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFD58E8E), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Agregar imagen",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ===== CAMPOS DE TEXTO =====
                LabeledTextField2(
                    label = "Nombre Producto:",
                    placeholder = "Nombre Producto",
                    labelColor = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledTextField2(
                    label = "Precio:",
                    placeholder = "Valor",
                    labelColor = Color(0xFFFFCC00)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledTextField2(
                    label = "Descripción Producto:",
                    placeholder = "Descripción Producto",
                    labelColor = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                LabeledTextField2(
                    label = "Dirección:",
                    placeholder = "Dirección",
                    labelColor = Color(0xFFFFCC00)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== BOTÓN GUARDAR =====
                Button(
                    onClick = {" " },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC00)),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Guardar",
                        color = Color(0xFF641717),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LabeledTextField2(label: String, placeholder: String, labelColor: Color) {
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