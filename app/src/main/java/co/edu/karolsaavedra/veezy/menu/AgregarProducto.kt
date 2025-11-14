package co.edu.karolsaavedra.veezy.menu

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import co.edu.karolsaavedra.veezy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun AgregarProductoScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var mensaje by remember { mutableStateOf("") }

    var nombreProducto by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }

    var precioError by remember { mutableStateOf("") }
    var nombreProductoError by remember { mutableStateOf("") }
    var direccionError by remember { mutableStateOf("") }
    var descripcionError by remember { mutableStateOf("") }
    var horarioError by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uid) {
        db.collection("restaurantes").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    direccion = document.getString("direccion") ?: ""
                    horario = document.getString("horario") ?: ""
                }
            }
            .addOnFailureListener {
                mensaje = "Error al cargar datos: ${it.message}"
            }
    }

    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var imagenSubidaUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Launcher para seleccionar imagen de la galería
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagenUri = uri
            Toast.makeText(context, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
            Log.d("AgregarProducto", "URI seleccionada: $uri")
        }
    }

    // Launcher para pedir permiso
    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galeriaLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }
    }

    fun abrirGaleria() {
        val permiso = if (android.os.Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED) {
            galeriaLauncher.launch("image/*")
        } else {
            permisoLauncher.launch(permiso)
        }
    }

    fun limpiarFormulario() {
        nombreProducto = ""
        precio = ""
        descripcion = ""
        imagenUri = null
        imagenSubidaUri = null
        nombreProductoError = ""
        precioError = ""
        descripcionError = ""
        direccionError = ""
        horarioError = ""
        mensaje = "Formulario limpiado"
        Toast.makeText(context, "Información eliminada", Toast.LENGTH_SHORT).show()
    }

    // Diálogo de confirmación para limpiar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Eliminar información",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar toda la información del formulario?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        limpiarFormulario()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = Color(0xFF641717))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // ===== HEADER CON BOTONES =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón volver
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Volver",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    // Botón eliminar
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar información",
                            tint = Color(0xFFFF6B6B),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // ===== CONTENEDOR DE IMAGEN =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color(0xFF8C3A3A), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val mostrarImagen = imagenSubidaUri ?: imagenUri

                    if (mostrarImagen != null) {
                        Image(
                            painter = rememberAsyncImagePainter(mostrarImagen),
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .clickable { abrirGaleria() },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color(0xFFD58E8E), CircleShape)
                                .clickable { abrirGaleria() },
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
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ===== CAMPO: NOMBRE DEL PRODUCTO =====
                Text(
                    text = "Nombre del producto:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00),
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

                // ===== CAMPO: PRECIO =====
                Text(
                    text = "Precio:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00),
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    placeholder = { Text("Precio", color = Color(0xFFB2B2B2)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false
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
                        if (precioError.isNotEmpty()) Text(precioError, color = Color.Red)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== CAMPO: DESCRIPCIÓN =====
                Text(
                    text = "Descripción producto:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00),
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
                        capitalization = KeyboardCapitalization.Sentences,
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
                    },
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ===== CAMPO: DIRECCIÓN =====
                Text(
                    text = "Dirección:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00),
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
                        capitalization = KeyboardCapitalization.Words,
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

                Spacer(modifier = Modifier.height(12.dp))

                // ===== CAMPO: HORARIO =====
                Text(
                    text = "Horario:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp),
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFFFFCC00),
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = horario,
                    onValueChange = { horario = it },
                    placeholder = { Text("Ej: Lun-Vie 9AM-6PM", color = Color(0xFFB2B2B2)) },
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
                        if (horarioError.isNotEmpty()) Text(horarioError, color = Color.Red)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== BOTÓN GUARDAR =====
                Button(
                    onClick = {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser == null) {
                            mensaje = "Debes iniciar sesión antes de subir un producto."
                            return@Button
                        }
                        val uid = currentUser.uid

                        if (nombreProducto.isBlank() || precio.isBlank() || descripcion.isBlank() ||
                            direccion.isBlank() || horario.isBlank()) {
                            mensaje = "Por favor completa todos los campos."
                            return@Button
                        }

                        if (imagenUri == null) {
                            mensaje = "Selecciona una imagen antes de guardar."
                            return@Button
                        }

                        // Subir imagen a Firebase Storage
                        val imageRef = FirebaseStorage.getInstance().reference
                            .child("restaurantes/$uid/productos/${System.currentTimeMillis()}.jpg")

                        mensaje = "Subiendo imagen..."

                        imageRef.putFile(imagenUri!!)
                            .addOnSuccessListener {
                                imageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val productoData = mapOf(
                                        "nombreProducto" to nombreProducto,
                                        "precio" to precio,
                                        "descripcion" to descripcion,
                                        "direccion" to direccion,
                                        "horario" to horario,
                                        "imagenUrl" to uri.toString()
                                    )

                                    // Guardar en Firestore
                                    FirebaseFirestore.getInstance()
                                        .collection("restaurantes")
                                        .document(uid)
                                        .collection("productos")
                                        .add(productoData)
                                        .addOnSuccessListener {
                                            mensaje = "Producto guardado correctamente."
                                            imagenSubidaUri = uri
                                            Toast.makeText(
                                                context,
                                                "Producto agregado exitosamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.navigate("menuRestauranteScreen") {
                                                popUpTo("menuRestauranteScreen") { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            mensaje = "Error al guardar en Firestore: ${e.message}"
                                        }
                                }.addOnFailureListener { e ->
                                    mensaje = "Error al obtener URL de imagen: ${e.message}"
                                }
                            }
                            .addOnFailureListener { e ->
                                mensaje = "Error al subir imagen: ${e.message}"
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = mensaje,
                        color = if (mensaje.contains("correctamente")) Color(0xFF4CAF50) else Color(0xFFFF6B6B),
                        modifier = Modifier.padding(top = 8.dp),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}