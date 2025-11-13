package co.edu.karolsaavedra.veezy.ViewRestaurante

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.validateConfirmPasswordRestaurante
import co.edu.karolsaavedra.veezy.validateDireccion
import co.edu.karolsaavedra.veezy.validateEmailRestaurante
import co.edu.karolsaavedra.veezy.validateHorarioRestaurante
import co.edu.karolsaavedra.veezy.validateNameRestaurante
import co.edu.karolsaavedra.veezy.validatePasswordRestaurante
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@Preview(showBackground = true)
@Composable
fun RegisterRestaurante(
    onSuccesfuRegisterRestaurante: () -> Unit = {},
    onClickBackRegisterRestaurante: () -> Unit = {},
) {
    var inputNombreRestaurante by remember { mutableStateOf("") }
    var inputDireccion by remember { mutableStateOf("") }
    var inputEmail by remember { mutableStateOf("") }
    var inputPassword by remember { mutableStateOf("") }
    var inputPasswordConfirm by remember { mutableStateOf("") }
    var inputHorarioRestaurante by remember { mutableStateOf("") }
    var productosRestaurante by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf("") }
    var direccionError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var passwordConfirmError by remember { mutableStateOf("") }
    var HorarioError by remember { mutableStateOf("") }
    var registerError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val activity = LocalView.current.context as Activity
    val auth = Firebase.auth
    val db = remember { FirebaseFirestore.getInstance() }

    Scaffold(
        topBar = {
            IconButton(
                onClick = { if (!isLoading) onClickBackRegisterRestaurante() },
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Volver",
                    tint = Color(0xFF641717),
                    modifier = Modifier.size(42.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFFAF0F0))
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Crear cuenta",
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF641717),
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0x80000000),
                        offset = Offset(0f, 8f),
                        blurRadius = 10f
                    )
                )
            )

            Spacer(modifier = Modifier.height(5.dp))

            // -------- Dirección --------
            Text(
                text = "Dirección",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFF863939)
                )
            )

            Spacer(modifier = Modifier.height(1.dp))

            OutlinedTextField(
                value = inputDireccion,
                onValueChange = {
                    inputDireccion = it
                    direccionError = if (it.isBlank()) {
                        "La dirección no puede estar vacía"
                    } else {
                        ""
                    }
                },
                label = { Text("Dirección", color = Color(0xFFCB6363)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Dirección",
                        tint = Color(0xFFCB6363)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true
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
                supportingText = {
                    when {
                        direccionError.isNotEmpty() -> Text(
                            direccionError,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                        else -> Text(
                            "Ejemplo: Cl. 32 #26-26, Bucaramanga, Santander",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // -------- Nombre restaurante --------
            Text(
                text = "Nombre restaurante",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputNombreRestaurante,
                onValueChange = { inputNombreRestaurante = it },
                label = { Text("Nombre restaurante", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre restaurante", tint = Color(0xFFCB6363)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true
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
                supportingText = { if (nombreError.isNotEmpty()) Text(nombreError, color = Color.Red) }
            )

            Spacer(modifier = Modifier.height(5.dp))

            // -------- Horario --------
            Text(
                text = "Horario de atención",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputHorarioRestaurante,
                onValueChange = { inputHorarioRestaurante = it },
                label = { Text("Horario de atención", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = "Horario de atención", tint = Color(0xFFCB6363)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true
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
                supportingText = { if (HorarioError.isNotEmpty()) Text(HorarioError, color = Color.Red) }
            )

            Spacer(modifier = Modifier.height(5.dp))

            // -------- Correo --------
            Text(
                text = "Correo especial",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputEmail,
                onValueChange = { inputEmail = it },
                label = { Text("Correo especial", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Correo", tint = Color(0xFFCB6363)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
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
                supportingText = { if (emailError.isNotEmpty()) Text(emailError, color = Color.Red) }
            )

            Spacer(modifier = Modifier.height(5.dp))

            // -------- Contraseña --------
            Text(
                text = "Contraseña",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputPassword,
                onValueChange = { inputPassword = it },
                label = { Text("Contraseña", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña", tint = Color(0xFFCB6363)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
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
                supportingText = { if (passwordError.isNotEmpty()) Text(passwordError, color = Color.Red) }
            )

            Spacer(modifier = Modifier.height(5.dp))

            // -------- Confirmar contraseña --------
            Text(
                text = "Confirmar Contraseña",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputPasswordConfirm,
                onValueChange = { inputPasswordConfirm = it },
                label = { Text("Confirmar Contraseña", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirmar", tint = Color(0xFFCB6363)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
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
                supportingText = { if (passwordConfirmError.isNotEmpty()) Text(passwordConfirmError, color = Color.Red) }
            )

            if (registerError.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(registerError, color = Color.Red, modifier = Modifier.padding(horizontal = 24.dp))
            }

            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = {
                    // Validaciones (usa tus helpers de restaurante)
                    val okNombre = validateNameRestaurante(inputNombreRestaurante).first
                    val okDireccion = validateDireccion(inputDireccion).first
                    val okEmail = validateEmailRestaurante(inputEmail).first
                    val okPassword = validatePasswordRestaurante(inputPassword).first
                    val okConfirm = validateConfirmPasswordRestaurante(inputPassword, inputPasswordConfirm).first
                    val okHorario = validateHorarioRestaurante(inputHorarioRestaurante).first



                    nombreError = validateNameRestaurante(inputNombreRestaurante).second
                    direccionError = validateDireccion(inputDireccion).second
                    emailError = validateEmailRestaurante(inputEmail).second
                    passwordError = validatePasswordRestaurante(inputPassword).second
                    passwordConfirmError = validateConfirmPasswordRestaurante(inputPassword, inputPasswordConfirm).second
                    HorarioError = validateHorarioRestaurante(inputHorarioRestaurante).second


                    if (!(okNombre && okDireccion && okEmail && okPassword && okConfirm && okHorario) || isLoading) {
                        registerError = if (!isLoading) "Hubo un error en el registro" else ""
                        return@Button
                    }

                    registerError = ""
                    isLoading = true

                    //  Crear usuario en Auth con EMAIL (no con nombre)
                    auth.createUserWithEmailAndPassword(inputEmail.trim(), inputPassword)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid
                                if (uid == null) {
                                    registerError = "No se pudo obtener el UID"
                                    isLoading = false
                                    return@addOnCompleteListener
                                }

                                //  Guardar perfil en Firestore: users/{uid}
                                val restaurante = hashMapOf(
                                    "uid" to uid,
                                    "nombreRestaurante" to inputNombreRestaurante,
                                    "direccion" to inputDireccion,
                                    "email" to inputEmail,
                                    "rol" to "restaurante",
                                    "createdAt" to Timestamp.now(),
                                    "horario" to inputHorarioRestaurante,
                                    "productos" to productosRestaurante
                                )

                                db.collection("restaurantes").document(uid)
                                    .set(restaurante)
                                    .addOnSuccessListener {
                                        onSuccesfuRegisterRestaurante()
                                    }
                                    .addOnFailureListener { e ->
                                        // Limpieza opcional si falla Firestore
                                        auth.currentUser?.delete()
                                        registerError = "No se pudo guardar el perfil: ${e.message}"
                                        isLoading = false
                                    }
                            } else {
                                val ex = task.exception
                                registerError = when (ex) {
                                    is FirebaseAuthInvalidCredentialsException -> "Correo inválido"
                                    is FirebaseAuthUserCollisionException -> "Correo ya registrado"
                                    else -> "Error al registrarse: ${ex?.localizedMessage ?: "desconocido"}"
                                }
                                isLoading = false
                            }
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF863939)),
                shape = RoundedCornerShape(40.dp),
                enabled = !isLoading,
                modifier = Modifier
                    .width(204.dp)
                    .height(49.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(40.dp),
                        clip = false
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(end = 0.dp)
                ) {
                    Text(
                        text = if (isLoading) "Creando..." else "Crear cuenta",
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 0.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .offset(x = 12.dp)
                            .background(color = Color(0xFFFFFFFF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.flecha_oscura),
                            contentDescription = "Flecha2",
                            tint = Color(0xFFE08A8A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
