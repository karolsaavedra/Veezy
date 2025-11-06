package co.edu.karolsaavedra.veezy.ViewCliente

import android.app.Activity
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import co.edu.karolsaavedra.veezy.validateEmail
import co.edu.karolsaavedra.veezy.validatePassword
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth

@Preview(showBackground = true)
@Composable
fun LoginClienteScreen(
    onSuccesfuloginCliente: () -> Unit = {},
    onClickloginCliente: () -> Unit = {},
    onClickBackLoginCliente: () -> Unit = {}

) {
    //Estados
    var inputEmail by remember { mutableStateOf("") }
    var inputPasswordCliente by remember { mutableStateOf("") }
    var loginErrorCliente by remember { mutableStateOf("") }
    var emailErrorCliente by remember { mutableStateOf("") }
    var passwordErrorlogincliente by remember { mutableStateOf("")}

    val activity = LocalView.current.context as Activity

    val auth = Firebase.auth


    Scaffold (
        topBar = {
            //  Flecha de retroceso en la parte superior izquierda
            IconButton(
                onClick = { onClickBackLoginCliente() },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(140.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically

            ) {
                Image(
                    painter = painterResource(id = R.drawable.veezy_logo),
                    contentDescription = "Logo Veezy",
                    modifier = Modifier
                        .size(80.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "eezy",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF641717),
                    fontFamily = FontFamily.SansSerif
                )

            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Iniciar sesión",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF641717),
                style = TextStyle(
                    shadow = Shadow(
                        color = Color(0x80000000), //  Negro semitransparente
                        offset = Offset(0f, 6f),
                        blurRadius = 8f            //  Difuminado (ajustable)
                    )
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
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
            Spacer(modifier = Modifier.height(5.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = inputEmail, // Valor vacío (sin estado)
                onValueChange = {inputEmail = it},
                label = { Text("Correo Electrónico",
                    modifier = Modifier,
                    color = Color(0xFFCB6363)
                ) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), //modificar el ancho del campo
                //mostrar mensaje de error por si algún dato quedó mal digitado
                supportingText = {
                    if (emailErrorCliente.isNotEmpty()){
                        Text(
                            text = emailErrorCliente,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(32.dp))
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
            Spacer(modifier = Modifier.height(5.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = inputPasswordCliente, // Valor vacío (sin estado)
                onValueChange = {inputPasswordCliente = it},
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

                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), //modificar el ancho del campo

                //mostrar mensaje de error por si algún dato quedó mal digitado
                supportingText = {
                    if (passwordErrorlogincliente.isNotEmpty()){
                        Text(
                            text = passwordErrorlogincliente,
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)

                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(32.dp))

            if (loginErrorCliente.isNotEmpty()){
                Text(
                    loginErrorCliente,
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    //TODA LA PARTE DE VALIDACIONES SE CONECTA CON VALIDATIONS.KT
                    //agregar la validación de datos, y que pueda ingresar correctamente
                    val isValidEmail: Boolean = validateEmail(inputEmail).first //.first devuelve el valor booleano, si necesitaramos el string se colocaría .second
                    val isValidPassword = validatePassword(inputPasswordCliente).first

                    //variables por si ocurre algún error al ingresar los datos
                    emailErrorCliente = validateEmail(inputEmail).second //.second va a devolver el String
                    passwordErrorlogincliente = validatePassword(inputPasswordCliente).second //.second va a devolver el String

                    if (isValidEmail && isValidPassword){ //validar tanto el email como la contraseña
                        // colocar datos para poder iniciar sesión
                        auth.signInWithEmailAndPassword(inputEmail, inputPasswordCliente)
                            .addOnCompleteListener (activity){ task ->
                                if (task.isSuccessful){
                                    onSuccesfuloginCliente()
                                }else{
                                    loginErrorCliente= when(task.exception){ //tipo de advertencias  de error que van a aparecer si la contraseña o correo están mal, o si no existe el correo
                                        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrecta"
                                        is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo"
                                        else -> "Error al iniciar sesión. Intenta de nuevo"
                                    }

                                }
                            }

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
                    //  Texto "Iniciar sesión"
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
                            .offset(x = 12.dp) //  Desplaza el círculo hacia afuera del borde
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
