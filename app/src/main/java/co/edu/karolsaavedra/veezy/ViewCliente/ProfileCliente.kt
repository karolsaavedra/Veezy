package co.edu.karolsaavedra.veezy.ViewCliente

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Preview(showBackground = true)
@Composable

fun ProfileClienteScreen( onClickLogout:  () -> Unit = {}) {

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    val auth = Firebase.auth
    val user = auth.currentUser
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // 🔹 ENCABEZADO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            color = Color(0xFF641717),
                            shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                        )
                ) {
                    Box(

                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        //  Aro superior izquierdo (más grande)
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .offset(x = (-180).dp, y = (-80).dp)
                                .border(
                                    width = 3.dp,
                                    color = Color(0xFFA979A7),
                                    shape = CircleShape
                                )
                        )

                        //Aro inferior derecho (pequeño)
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .offset(x = (180).dp, y = (30).dp)
                                .border(
                                    width = 2.dp,
                                    color = Color(0xFFA979A7),
                                    shape = CircleShape
                                )
                        )
                        // Aro inferior izquierdo (pequeño)
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .offset(x = (-170).dp, y = (-70).dp)
                                .border(
                                    width = 2.dp,
                                    color = Color(0xFFA979A7),
                                    shape = CircleShape
                                )
                        )

                        // Aro inferior derecho (más grande)
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .offset(x = (177).dp, y = (20).dp)
                                .border(
                                    width = 3.dp,
                                    color = Color(0xFFA979A7),
                                    shape = CircleShape
                                )
                        )
                        }

                    // 🔹 Contenido central
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.White, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = Color(0xFF641717),
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Karol Saavedra",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )

                        Text(
                            text = "Reservas hechas",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        Text(
                            text = "10",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 🔹 INFORMACIÓN DEL USUARIO
                Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
                    OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") })

                    Spacer(modifier = Modifier.height(28.dp))

                    // 🔹 Botón "Cerrar sesión"
                    Button(
                        onClick = { auth.signOut()
                            onClickLogout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD99C00)
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(48.dp)
                            .shadow(4.dp, RoundedCornerShape(50.dp),

                                )
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // 🔹 Barra inferior
            BottomBar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    @Composable
    fun InfoItem(label: String, value: String) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF641717)
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color(0xFF641717).copy(alpha = 0.3f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
