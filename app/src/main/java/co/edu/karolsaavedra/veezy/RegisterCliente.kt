package co.edu.karolsaavedra.veezy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable

fun RegisterCliente(
    onSuccesfuRegisterCliente: () -> Unit = {},
    //onClickRegisterCliente: () -> Unit = {}
){
    var inputname by remember { mutableStateOf("") }
    var inputapellido by remember { mutableStateOf("") }
    var inputEmailRegister by remember { mutableStateOf("") }
    var inputpassword by remember { mutableStateOf("") }
    var inputConfrimpassword by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var apellidoError by remember { mutableStateOf("") }
    var EmailErrorRegister by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("")}
    var ConfrimpasswordError by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.imePadding()//hacer que el teclado no tape los campos que se van a llenar
                //.verticalScroll(rememberScrollState())//hacer que el teclado no tape los campos que se van a llenar
                .background(Color(0xFFFAF0F0))
                .padding(paddingValues),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Crear cuenta",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF641717)
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
            Spacer(modifier = Modifier.height(5.dp))

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

                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
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
            Spacer(modifier = Modifier.height(5.dp))

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

                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
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
            Spacer(modifier = Modifier.height(5.dp))

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

                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
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
            Spacer(modifier = Modifier.height(5.dp))

            // Campo de Correo Electrónico
            OutlinedTextField(
                value = inputpassword, // Valor vacío (sin estado)
                onValueChange = {inputpassword = it},
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),

                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
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
                    if (passwordError.isNotEmpty()){
                        Text(
                            text = passwordError,
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
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = inputConfrimpassword, // Valor vacío (sin estado)
                onValueChange = {inputConfrimpassword = it},
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),

                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
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
                    if (ConfrimpasswordError.isNotEmpty()){
                        Text(
                            text = ConfrimpasswordError,
                            color = Color.Red
                        )
                    }
                }

            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {

                },modifier = Modifier

                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF863939))
            ) {
                Text(
                    text = "Crear cuenta",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
}
}
}
