package co.edu.karolsaavedra.veezy.ViewCliente

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun ProfileClienteScreen(
    navController: NavHostController,
    onClickLogout: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    val auth = Firebase.auth
    val user = auth.currentUser

    // Si el usuario está autenticado, se muestran sus datos
    LaunchedEffect(user) {
        user?.let {
            nombre = it.displayName ?: ""
            correo = it.email ?: ""
        }
    }

    Scaffold(
        containerColor = Color(0xFF641717),
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
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
                        // Aros decorativos
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

                    //  Contenido central
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
                            text = nombre.ifEmpty { "Usuario" },
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

                //  INFORMACIÓN DEL USUARIO
                Column(modifier = Modifier.padding(horizontal = 30.dp)) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    //  Botón "Cerrar sesión"
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut() // Cierra la sesión de Firebase
                            navController.navigate("loginCliente") {  // nombre  de la ruta en el NavHost
                                popUpTo("menuScreen") { inclusive = true } // elimina pantallas previas
                                }
                                  },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD99C00)
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(48.dp)
                            .shadow(4.dp, RoundedCornerShape(50.dp))
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
            BottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController,
                isBackgroundWine = false
            )
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
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileClienteScreen() {
    val navController = rememberNavController()
    ProfileClienteScreen(navController = navController)
}