package co.edu.karolsaavedra.veezy.ViewCliente

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import co.edu.karolsaavedra.veezy.R
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
import co.edu.karolsaavedra.veezy.validateConfirmPassword
import co.edu.karolsaavedra.veezy.validateEmail
import co.edu.karolsaavedra.veezy.validateLastName
import co.edu.karolsaavedra.veezy.validateName
import co.edu.karolsaavedra.veezy.validatePassword
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth

@Preview(showBackground = true)
@Composable

fun RegisterCliente(
    onSuccesfuRegisterCliente: () -> Unit = {},
    onClickBackRegister: () -> Unit = {}
){
    var inputname by remember { mutableStateOf("") }
    var inputapellido by remember { mutableStateOf("") }
    var inputEmailRegister by remember { mutableStateOf("") }
    var inputpassworRegister by remember { mutableStateOf("") }
    var inputConfirmpasswordRegister by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var apellidoError by remember { mutableStateOf("") }
    var EmailErrorRegister by remember { mutableStateOf("") }
    var passwordErrorRegister by remember { mutableStateOf("")}
    var ConfrimpasswordErrorRegister by remember { mutableStateOf("") }
    var registerError by remember { mutableStateOf("") }
    val activity = LocalView.current.context as Activity

    val auth = Firebase.auth

    Scaffold (
        topBar = {
            // 🔹 Flecha de retroceso en la parte superior izquierda
            IconButton(
                onClick = { onClickBackRegister() },
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
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()//hacer que el teclado no tape los campos que se van a llenar
                .verticalScroll(rememberScrollState())//hacer que el teclado no tape los campos que se van a llenar
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
                        color = Color(0x80000000), //  Negro semitransparente (ajustable)
                        offset = Offset(0f, 8f),   //  Sombra hacia abajo
                        blurRadius = 10f           //  Difuminado / intensidad
                    )
                )
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Nombre",
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

            // Campo de Nombre
            OutlinedTextField(
                value = inputname, // Valor vacío (sin estado)
                onValueChange = {inputname = it},
                label = { Text("Nombre",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person",
                        tint = Color(0xFFCB6363) // Color gris
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),

                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    // Color del texto y el cursor cuando se está escribiendo
                    focusedTextColor = Color(0xFF641717),
                    focusedBorderColor = Color(0xFF641717),
                    focusedLabelColor = Color(0xFF641717),
                    // Color del cursor
                    cursorColor = Color(0xFF641717),
                    // Color del contenedor (fondo del campo)
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    // Color del borde cuando no está seleccionado
                    unfocusedBorderColor = Color.Gray,
                ),
                //mostrar mensaje de error por si algún dato quedó mal digitado
                supportingText = {
                    if (nameError.isNotEmpty()){
                        Text(
                            text = nameError,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Apellido",
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
            // Campo de Correo Electrónico
            OutlinedTextField(
                value = inputapellido, // Valor vacío (sin estado)
                onValueChange = {inputapellido = it},
                label = { Text("Apellido",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Apellido",
                        tint = Color(0xFFCB6363)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),

                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    // Color del texto y el cursor cuando se está escribiendo
                    focusedTextColor = Color(0xFF641717),
                    focusedBorderColor = Color(0xFF641717),
                    focusedLabelColor = Color(0xFF641717),
                    // Color del cursor
                    cursorColor = Color(0xFF641717),
                    // Color del contenedor (fondo del campo)
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    // Color del borde cuando no está seleccionado
                    unfocusedBorderColor = Color.Gray,
                ),
                supportingText = {
                    if (apellidoError.isNotEmpty()){
                        Text(
                            text = apellidoError,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Correo",
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

// Campo de Correo Electrónico
            OutlinedTextField(
                value = inputEmailRegister, // Valor vacío (sin estado)
                onValueChange = {inputEmailRegister = it},
                label = { Text("Correo",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo",
                        tint = Color(0xFFCB6363)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),

                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    // Color del texto y el cursor cuando se está escribiendo
                    focusedTextColor = Color(0xFF641717),
                    focusedBorderColor = Color(0xFF641717),
                    focusedLabelColor = Color(0xFF641717),
                    // Color del cursor
                    cursorColor = Color(0xFF641717),
                    // Color del contenedor (fondo del campo)
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    // Color del borde cuando no está seleccionado
                    unfocusedBorderColor = Color.Gray,
                ),
                supportingText = {
                    if (EmailErrorRegister.isNotEmpty()){
                        Text(
                            text = EmailErrorRegister,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Contraseña",
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

// Campo de Correo Electrónico
            OutlinedTextField(
                value = inputpassworRegister, // Valor vacío (sin estado)
                onValueChange = {inputpassworRegister = it},
                label = { Text("Contraseña",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = Color(0xFFCB6363)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),

                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    // Color del texto y el cursor cuando se está escribiendo
                    focusedTextColor = Color(0xFF641717),
                    focusedBorderColor = Color(0xFF641717),
                    focusedLabelColor = Color(0xFF641717),
                    // Color del cursor
                    cursorColor = Color(0xFF641717),
                    // Color del contenedor (fondo del campo)
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    // Color del borde cuando no está seleccionado
                    unfocusedBorderColor = Color.Gray,
                ),
                supportingText = {
                    if (passwordErrorRegister.isNotEmpty()){
                        Text(
                            text = passwordErrorRegister,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Confirmar Contraseña",
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
                value = inputConfirmpasswordRegister, // Valor vacío (sin estado)
                onValueChange = {inputConfirmpasswordRegister = it},
                label = { Text("Confirmar Contraseña",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Email",
                        tint = Color(0xFFCB6363) // Color gris
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),

                shape = RoundedCornerShape(50.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    // Color del texto y el cursor cuando se está escribiendo
                    focusedTextColor = Color(0xFF641717),
                    focusedBorderColor = Color(0xFF641717),
                    focusedLabelColor = Color(0xFF641717),
                    // Color del cursor
                    cursorColor = Color(0xFF641717),
                    // Color del contenedor (fondo del campo)
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    // Color del borde cuando no está seleccionado
                    unfocusedBorderColor = Color.Gray,
                ),
                //mostrar mensaje de error por si algún dato quedó mal digitado
                supportingText = {
                    if (ConfrimpasswordErrorRegister.isNotEmpty()){
                        Text(
                            text = ConfrimpasswordErrorRegister,
                            color = Color.Red
                        )
                    }
                }

            )
            if (registerError.isNotEmpty()){
                Text (registerError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    val isValidName = validateName(inputname).first
                    val isValidLastName = validateLastName(inputapellido).first
                    val isValidEmail = validateEmail(inputEmailRegister).first
                    val isValidPassword = validatePassword(inputpassworRegister).first
                    val isValidConfirmPassword = validateConfirmPassword(inputpassworRegister, inputConfirmpasswordRegister).first //crear y confirmar contraseña

                    nameError = validateName(inputname).second
                    apellidoError = validateLastName(inputapellido).second
                    EmailErrorRegister= validateEmail(inputEmailRegister). second
                    passwordErrorRegister = validatePassword(inputpassworRegister).second
                    ConfrimpasswordErrorRegister = validateConfirmPassword(inputpassworRegister,inputConfirmpasswordRegister).second

                    if (isValidName && isValidLastName && isValidEmail && isValidPassword && isValidConfirmPassword){
                        auth.createUserWithEmailAndPassword(inputEmailRegister, inputpassworRegister)
                            .addOnCompleteListener(activity) { task->
                                if (task.isSuccessful){
                                    onSuccesfuRegisterCliente()
                                }else{
                                    registerError = when(task.isSuccessful){
                                        is FirebaseAuthInvalidCredentialsException -> "Correo invalido"
                                        is FirebaseAuthUserCollisionException -> "Correo ya registrado"
                                        else -> "Error al registrarse"
                                    }
                                }
                            }

                    }else{
                        registerError = "Hubo un error en el registro"
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF863939)
                ),
                shape = RoundedCornerShape(40.dp),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 0.dp) // sin margen derecho extra
                ) {
                    // 🔹 Texto "Iniciar sesión"
                    Text(
                        text = "Iniciar sesión",
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 0.dp)
                    )

                    //  Círculo rojo con la flecha (ligeramente salido del borde)
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .offset(x = 12.dp) // 🔸 Desplaza el círculo hacia afuera del borde
                            .background(
                                color = Color(0xFFFFFFFF),
                                shape = CircleShape
                            ),
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
        }
    }
}
