package co.edu.karolsaavedra.veezy.ViewCliente

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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
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
import co.edu.karolsaavedra.veezy.validateConfirmPassword
import co.edu.karolsaavedra.veezy.validateEmail
import co.edu.karolsaavedra.veezy.validateLastName
import co.edu.karolsaavedra.veezy.validateName
import co.edu.karolsaavedra.veezy.validatePassword
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@Preview(showBackground = true)
@Composable
fun RegisterCliente(
    onSuccesfuRegisterCliente: () -> Unit = {},
    onClickBackRegister: () -> Unit = {}
) {
    var inputname by remember { mutableStateOf("") }
    var inputapellido by remember { mutableStateOf("") }
    var inputEmailRegister by remember { mutableStateOf("") }
    var inputpassworRegister by remember { mutableStateOf("") }
    var inputConfirmpasswordRegister by remember { mutableStateOf("") }
    var TurnoCliente by remember { mutableStateOf("") }


    var nameError by remember { mutableStateOf("") }
    var apellidoError by remember { mutableStateOf("") }
    var EmailErrorRegister by remember { mutableStateOf("") }
    var passwordErrorRegister by remember { mutableStateOf("") }
    var ConfrimpasswordErrorRegister by remember { mutableStateOf("") }
    var registerError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val activity = LocalView.current.context as Activity
    val auth = Firebase.auth
    val db = remember { FirebaseFirestore.getInstance() }




    Scaffold(
        topBar = {
            IconButton(
                onClick = { if (!isLoading) onClickBackRegister() },
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
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

            Spacer(modifier = Modifier.height(10.dp))
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

            Spacer(modifier = Modifier.height(28.dp))

            // --- Nombre ---
            Text(
                text = "Nombre",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputname,
                onValueChange = { inputname = it },
                label = { Text("Nombre", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre", tint = Color(0xFFCB6363)) },
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
                    if (nameError.isNotEmpty()) Text(text = nameError, color = Color.Red)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- Apellido ---
            Text(
                text = "Apellido",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputapellido,
                onValueChange = { inputapellido = it },
                label = { Text("Apellido", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Apellido", tint = Color(0xFFCB6363)) },
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
                    if (apellidoError.isNotEmpty()) Text(text = apellidoError, color = Color.Red)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- Correo ---
            Text(
                text = "Correo",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputEmailRegister,
                onValueChange = { inputEmailRegister = it },
                label = { Text("Correo", color = Color(0xFFCB6363)) },
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
                supportingText = {
                    if (EmailErrorRegister.isNotEmpty()) Text(text = EmailErrorRegister, color = Color.Red)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- Contraseña ---
            Text(
                text = "Contraseña",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputpassworRegister,
                onValueChange = { inputpassworRegister = it },
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
                supportingText = {
                    if (passwordErrorRegister.isNotEmpty()) Text(text = passwordErrorRegister, color = Color.Red)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- Confirmar Contraseña ---
            Text(
                text = "Confirmar Contraseña",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(fontSize = 25.sp, fontFamily = FontFamily.SansSerif, color = Color(0xFF863939))
            )
            Spacer(modifier = Modifier.height(1.dp))
            OutlinedTextField(
                value = inputConfirmpasswordRegister,
                onValueChange = { inputConfirmpasswordRegister = it },
                label = { Text("Confirmar Contraseña", color = Color(0xFFCB6363)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirmar contraseña", tint = Color(0xFFCB6363)) },
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
                supportingText = {
                    if (ConfrimpasswordErrorRegister.isNotEmpty()) Text(text = ConfrimpasswordErrorRegister, color = Color.Red)
                }
            )

            if (registerError.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(registerError, color = Color.Red, modifier = Modifier.padding(horizontal = 24.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // Validaciones
                    val isValidName = validateName(inputname).first
                    val isValidLastName = validateLastName(inputapellido).first
                    val isValidEmail = validateEmail(inputEmailRegister).first
                    val isValidPassword = validatePassword(inputpassworRegister).first
                    val isValidConfirmPassword = validateConfirmPassword(
                        inputpassworRegister,
                        inputConfirmpasswordRegister
                    ).first

                    nameError = validateName(inputname).second
                    apellidoError = validateLastName(inputapellido).second
                    EmailErrorRegister = validateEmail(inputEmailRegister).second
                    passwordErrorRegister = validatePassword(inputpassworRegister).second
                    ConfrimpasswordErrorRegister =
                        validateConfirmPassword(inputpassworRegister, inputConfirmpasswordRegister).second

                    if (!(isValidName && isValidLastName && isValidEmail && isValidPassword && isValidConfirmPassword) || isLoading) {
                        registerError = if (!isLoading) "Hubo un error en el registro" else ""
                        return@Button
                    }

                    registerError = ""
                    isLoading = true

                    auth.createUserWithEmailAndPassword(inputEmailRegister.trim(), inputpassworRegister)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid
                                if (uid == null) {
                                    registerError = "No se pudo obtener el UID"
                                    isLoading = false
                                    return@addOnCompleteListener
                                }

                                val user = hashMapOf(
                                    "uid" to uid,
                                    "nombre" to inputname,
                                    "apellido" to inputapellido,
                                    "email" to inputEmailRegister,
                                    "rol" to "cliente",
                                    "createdAt" to Timestamp.now(),
                                    "turno" to TurnoCliente
                                )

                                db.collection("clientes").document(uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        onSuccesfuRegisterCliente()
                                    }
                                    .addOnFailureListener { e ->
                                        // Limpieza opcional: borrar el usuario de Auth si Firestore falla
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
