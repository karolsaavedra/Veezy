package co.edu.karolsaavedra.veezy.ViewRestaurante



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
import androidx.compose.material.icons.filled.LocationOn
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

fun RegisterRestaurante(
    onSuccesfuRegisterCliente: () -> Unit = {},
    onClickBackRegisterRestaurante: () -> Unit = {},
    onClickRegisterRestaurante: () -> Unit = {}
){
    var inputnamerestaurante by remember { mutableStateOf("") }
    var inputDirecciónRestaurante by remember { mutableStateOf("") }
    var inputEmailRestauranteRegister by remember { mutableStateOf("") }
    var inputpassworRestauranteRegister by remember { mutableStateOf("") }
    var inputConfirmRestaurantepasswordRegister by remember { mutableStateOf("") }
    var NameRestauranteError by remember { mutableStateOf("") }
    var DireccionError by remember { mutableStateOf("") }
    var EmailRestauranteErrorRegister by remember { mutableStateOf("") }
    var passwordRestauranteErrorRegister by remember { mutableStateOf("")}
    var ConfrimpasswordRestauranteErrorRegister by remember { mutableStateOf("") }
    var registerRestauranteError by remember { mutableStateOf("") }
    val activity = LocalView.current.context as Activity

    val auth = Firebase.auth

    Scaffold (
        topBar = {
            //  Flecha de retroceso en la parte superior izquierda
            IconButton(
                onClick = { onClickBackRegisterRestaurante() },
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

            // Campo de Nombre
            OutlinedTextField(
                value = inputDirecciónRestaurante, // Valor vacío (sin estado)
                onValueChange = {inputDirecciónRestaurante = it},
                label = { Text("Dirección",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
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
                    if (DireccionError.isNotEmpty()){
                        Text(
                            text = DireccionError,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Nombre restaurante",
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

            // Campo de Nombre restaurante
            OutlinedTextField(
                value = inputnamerestaurante, // Valor vacío (sin estado)
                onValueChange = {inputnamerestaurante = it},
                label = { Text("Nombre restaurante",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nombre restaurante",
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
                    if (NameRestauranteError.isNotEmpty()){
                        Text(
                            text = NameRestauranteError,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Correo especial",
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
                value = inputEmailRestauranteRegister, // Valor vacío (sin estado)
                onValueChange = {inputEmailRestauranteRegister = it},
                label = { Text("Correo especial",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo especial",
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
                    if (EmailRestauranteErrorRegister.isNotEmpty()){
                        Text(
                            text = EmailRestauranteErrorRegister,
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
                value = inputpassworRestauranteRegister, // Valor vacío (sin estado)
                onValueChange = {inputpassworRestauranteRegister = it},
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
                    if (passwordRestauranteErrorRegister.isNotEmpty()){
                        Text(
                            text = passwordRestauranteErrorRegister,
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
                value = inputConfirmRestaurantepasswordRegister, // Valor vacío (sin estado)
                onValueChange = {inputConfirmRestaurantepasswordRegister = it},
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
                    if (ConfrimpasswordRestauranteErrorRegister.isNotEmpty()){
                        Text(
                            text = ConfrimpasswordRestauranteErrorRegister,
                            color = Color.Red
                        )
                    }
                }

            )
            if (registerRestauranteError.isNotEmpty()){
                Text (registerRestauranteError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    val isValidNameRestaurante = validateName(inputnamerestaurante).first
                    val isvalidateDireccion = validateName(inputDirecciónRestaurante).first
                    val isvalidateEmailRestaurante = validateEmail(inputEmailRestauranteRegister).first
                    val isvalidatePasswordRestaurante = validatePassword(inputpassworRestauranteRegister).first
                    val isvalidateConfirmPasswordRestaurante = validateConfirmPassword(inputpassworRestauranteRegister, inputConfirmRestaurantepasswordRegister).first //crear y confirmar contraseña

                    NameRestauranteError = validateName(inputnamerestaurante).second
                    DireccionError = validateLastName(inputDirecciónRestaurante).second
                    EmailRestauranteErrorRegister= validateEmail(inputEmailRestauranteRegister). second
                    passwordRestauranteErrorRegister = validatePassword(inputpassworRestauranteRegister).second
                    ConfrimpasswordRestauranteErrorRegister = validateConfirmPassword(inputpassworRestauranteRegister,inputConfirmRestaurantepasswordRegister).second

                    if (isvalidateEmailRestaurante && isvalidateDireccion && isValidNameRestaurante && isvalidatePasswordRestaurante && isvalidateConfirmPasswordRestaurante){
                        auth.createUserWithEmailAndPassword(inputnamerestaurante, inputpassworRestauranteRegister)
                            .addOnCompleteListener(activity) { task->
                                if (task.isSuccessful){
                                    onSuccesfuRegisterCliente()
                                }else{
                                    registerRestauranteError = when(task.isSuccessful){
                                        is FirebaseAuthInvalidCredentialsException -> "Correo invalido"
                                        is FirebaseAuthUserCollisionException -> "Correo ya registrado"
                                        else -> "Error al registrarse"
                                    }
                                }
                            }

                    }else{
                        registerRestauranteError = "Hubo un error en el registro"
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
                            .offset(x = 12.dp) // Desplaza el círculo hacia afuera del borde
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
