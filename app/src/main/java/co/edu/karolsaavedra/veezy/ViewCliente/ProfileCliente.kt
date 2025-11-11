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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileClienteScreen(
    navController: NavHostController,
    onClickLogout: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    //  Cargar datos desde Firestore
    LaunchedEffect(user) {
        if (user != null) {
            val uid = user.uid
            db.collection("clientes").document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    if (doc.exists()) {
                        nombre = doc.getString("nombre") ?: ""
                        apellido = doc.getString("apellido") ?: ""
                        correo = doc.getString("email") ?: user.email.orEmpty()
                    }
                    isLoading = false
                }
                .addOnFailureListener {
                    isLoading = false
                }
        } else {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
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
                //  Encabezado superior con avatar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            color = Color(0xFF641717),
                            shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                        )
                ) {
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
                            text = correo.ifEmpty { "correo@ejemplo.com" },
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                //  Información del cliente
                Column(modifier = Modifier.padding(horizontal = 30.dp)) {

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )

                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { },
                        label = { Text("Correo") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // 🔹 Botón Cerrar sesión
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("loginCliente") {
                                popUpTo("menuScreen") { inclusive = true }
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
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileClienteScreen() {
    val navController = rememberNavController()
    ProfileClienteScreen(navController = navController)
}
