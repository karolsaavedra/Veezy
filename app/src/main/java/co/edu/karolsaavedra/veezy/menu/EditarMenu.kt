package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun EditarMenuScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid

    var nombreRestaurante by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var inputnombreProducto by remember { mutableStateOf("") }
    var inputprecio by remember { mutableStateOf("") }

    var nombreProductoError by remember { mutableStateOf("") }
    var precioError by remember { mutableStateOf("") }

    var direccionError by remember { mutableStateOf("") }

    var mensaje by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }



    // ===== Cargar datos actuales =====
    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val snapshot = db.collection("restaurantes").document(uid).get().await()
                nombreRestaurante = snapshot.getString("nombreRestaurante") ?: ""
                direccion = snapshot.getString("direccion") ?: ""
                horario = snapshot.getString("horario") ?: ""
                // Si tu producto se guarda en otra colección, puedes traerlo aquí también.
            } catch (e: Exception) {
                mensaje = "Error al cargar datos: ${e.message}"
            }
        }
        isLoading = false
    }

    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF641717))
            }
        } else {
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

            // ===== FLECHA VOLVER =====
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 40.dp, start = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

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
                    .height(500.dp)
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3F3)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 16.dp)
                ) {
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

                    // ===== CAMPOS DE EDICIÓN =====
                    Text(
                        text = "Nombre del producto",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939), fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(1.dp))

                    OutlinedTextField(
                        value = inputnombreProducto,
                        onValueChange = { inputnombreProducto = it },
                        label = { Text("Nombre del producto", color = Color(0xFFCB6363)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false
                        ),
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF641717),
                            focusedBorderColor = Color(0xFF641717),
                            focusedLabelColor = Color(0xFF641717),
                            cursorColor = Color(0xFF641717),
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color.Gray,
                        ),
                        supportingText = { if (nombreProductoError.isNotEmpty()) Text(nombreProductoError, color = Color.Red) }
                    )
                    Spacer(modifier = Modifier.height(2.dp))


                    Text(
                        text = "Horario de atención",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939), fontWeight = FontWeight.Bold)
                    )

                    OutlinedTextField(
                        value = horario,
                        onValueChange = { horario = it },
                        placeholder = { Text("Horario de atención", color = Color(0xFFCB6363)) }, //  texto dentro editable
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false
                        ),
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF641717),
                            focusedBorderColor = Color(0xFF641717),
                            cursorColor = Color(0xFF641717),
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color.Gray,
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Precio",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                        textAlign = TextAlign.Start,
                        style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFFD4A017),fontWeight = FontWeight.Bold)
                    )

                    OutlinedTextField(
                        value = inputprecio,
                        onValueChange = { inputprecio = it },
                        placeholder = { Text("Precio", color = Color(0xFFD4A017)) }, // texto dentro, editable
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number, // tipo numérico
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false
                        ),
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF641717),
                            focusedBorderColor = Color(0xFFD4A017), // borde dorado al enfocar
                            cursorColor = Color(0xFF641717),
                            unfocusedContainerColor = Color(0xFFFFFFFF),
                            focusedContainerColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color.Gray,
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (inputnombreProducto.isBlank() || horario.isBlank() || inputprecio.isBlank()) {
                            mensaje = "Por favor complete todos los campos antes de guardar."
                        } else if(uid != null) {
                                val updates = hashMapOf(
                                    "nombreRestaurante" to nombreRestaurante,
                                    "direccion" to direccion,
                                    "horario" to horario,
                                    "productos" to inputnombreProducto,
                                    "precio" to inputprecio
                                )
                                db.collection("restaurantes").document(uid)
                                    .update(updates as Map<String, Any>)
                                    .addOnSuccessListener {
                                        mensaje = "Datos actualizados correctamente."
                                    }
                                    .addOnFailureListener {
                                        mensaje = "Error al actualizar: ${it.message}"
                                    }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC00)),
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .width(200.dp)
                            .height(45.dp)
                    ) {
                        Text(
                            text = "Guardar cambios",
                            color = Color(0xFF641717),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    if (mensaje.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = mensaje, color = Color(0xFF641717), fontSize = 14.sp)
                    }
                }
            }


                // ===== BOTÓN SIGUIENTE =====
                Button(
                    onClick = { navController.navigate("agregarProducto") },
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
                BottomBarRestaurante(
                    navController = navController,
                    isBackgroundWine = false,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun LabeledTextField(label: String, value: String, onValueChange: (String) -> Unit, labelColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = labelColor,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Ingrese $label", color = Color(0xFFBDBDBD)) },
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
fun EditarMenuScreenPreview() {
    val navController = rememberNavController()
    EditarMenuScreen(navController)
}
