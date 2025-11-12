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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun AgregarProductoScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var mensaje by remember { mutableStateOf("") }

    var nombreProducto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    var precioError by remember { mutableStateOf("") }
    var nombreProductoError by remember { mutableStateOf("") }
    var direccionError by remember { mutableStateOf("") }
    var descripcionError by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        db.collection("restaurantes").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    nombreProducto = document.getString("productos") ?: ""
                    precio = document.getString("precio") ?: ""
                    descripcion = document.getString("descripcion") ?: ""
                    direccion = document.getString("direccion") ?: ""
                }
            }
            .addOnFailureListener {
                mensaje = "Error al cargar datos: ${it.message}"
            }
    }


    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF641717))
        ) {
            val scrollState = rememberScrollState()

            // ===== CONTENIDO PRINCIPAL =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                IconButton(
                    onClick = {navController.popBackStack()},
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
                Text(
                    text = "Nombre del producto:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00), // Amarillo
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))


                OutlinedTextField(
                    value = nombreProducto,
                    onValueChange = { nombreProducto = it },
                    placeholder = { Text("Nombre del producto", color = Color(0xFFB2B2B2)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF641717),
                        unfocusedTextColor = Color(0xFF641717),
                        focusedBorderColor = Color(0xFF641717),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF641717),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    supportingText = {
                        if (nombreProductoError.isNotEmpty()) Text(nombreProductoError, color = Color.Red)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Precio:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00), // Amarillo
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))


                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    placeholder = { Text("Precio", color = Color(0xFFB2B2B2)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF641717),
                        unfocusedTextColor = Color(0xFF641717),
                        focusedBorderColor = Color(0xFF641717),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF641717),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    supportingText = {
                        if (precioError.isNotEmpty()) Text(precio, color = Color.Red)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Descripción producto:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00), // Amarillo
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))


                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = { Text("Descripción producto", color = Color(0xFFB2B2B2)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF641717),
                        unfocusedTextColor = Color(0xFF641717),
                        focusedBorderColor = Color(0xFF641717),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF641717),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    supportingText = {
                        if (descripcionError.isNotEmpty()) Text(descripcionError, color = Color.Red)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Dirección:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00), // Amarillo
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))


                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    placeholder = { Text("Dirección", color = Color(0xFFB2B2B2)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF641717),
                        unfocusedTextColor = Color(0xFF641717),
                        focusedBorderColor = Color(0xFF641717),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF641717),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    supportingText = {
                        if (direccionError.isNotEmpty()) Text(direccionError, color = Color.Red)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== BOTÓN GUARDAR =====
                Button(
                    onClick = {
                        if (nombreProducto.isBlank() || precio.isBlank() || descripcion.isBlank() || direccion.isBlank()) {
                            mensaje = " Por favor completa todos los campos."
                        } else {

                            val updates = mapOf(
                                "nombreProducto" to nombreProducto,
                                "precio" to precio,
                                "descripcion" to descripcion,
                                "direccion" to direccion
                            )

                            db.collection("restaurantes").document(uid)
                                .set(updates, com.google.firebase.firestore.SetOptions.merge())
                                .addOnSuccessListener {
                                    mensaje = "Datos guardados correctamente."
                                }
                                .addOnFailureListener {
                                    mensaje = "Error al guardar: ${it.message}"
                                    }
                                navController.navigate("menuRestaurante")
                            }

                    },
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
                if (mensaje.isNotEmpty()) {
                    Text(
                        text = mensaje,
                        color = when {
                            mensaje.contains("correctamente") -> Color.Green
                            else -> Color.Red
                        },
                        modifier = Modifier.padding(top = 8.dp)
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
@Preview(showBackground = true)
@Composable
fun AgregarProductoScreenPreview() {
    val navController = rememberNavController()
    AgregarProductoScreen(navController = navController)
}