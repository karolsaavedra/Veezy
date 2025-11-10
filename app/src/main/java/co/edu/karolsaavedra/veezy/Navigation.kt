package co.edu.karolsaavedra.veezy

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.ViewCliente.*
import co.edu.karolsaavedra.veezy.ViewGeneral.*
import co.edu.karolsaavedra.veezy.ViewRestaurante.*
import co.edu.karolsaavedra.veezy.menu.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Se inicializa como null para saber cuándo mostrar el NavHost
    var startDestination by remember { mutableStateOf<String?>(null) }

    // Verificar si el usuario ya está autenticado y cuál es su rol
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("usuarios").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    startDestination = when (doc.getString("rol")) {
                        "cliente" -> "menuScreen"
                        "restaurante" -> "menuRestaurante"
                        else -> "chooseRole"
                    }
                }
                .addOnFailureListener {
                    startDestination = "chooseRole"
                }
        } else {
            startDestination = "startApp"
        }
    }

    // Mientras no se haya determinado el destino inicial, muestra una pantalla de carga
    if (startDestination == null) {
        LoadingScreen()
    } else {
        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {
            // Pantalla inicial
            composable("startApp") {
                StartScreen(
                    onClickRegisterCliente = { navController.navigate("registerCliente") },
                    onClickRegisterRestaurante = { navController.navigate("registerRestaurante") },
                    onClickStartapp = { navController.navigate("chooseRole") }
                )
            }

            // Registro cliente
            composable("registerCliente") {
                RegisterCliente(
                    onSuccesfuRegisterCliente = { navController.navigate("loginCliente") { popUpTo(0) } },
                    onClickBackRegister = { navController.popBackStack() }
                )
            }

            // Registro restaurante
            composable("registerRestaurante") {
                RegisterRestaurante(
                    onSuccesfuRegisterCliente = { navController.navigate("loginRestaurante") { popUpTo(0) } },
                    onClickBackRegisterRestaurante = { navController.popBackStack() }
                )
            }

            // Elección de rol
            composable("chooseRole") {
                ChooseRoleScreen(
                    onClickCliente = { navController.navigate("loginCliente") },
                    onClickRestaurante = { navController.navigate("loginRestaurante") },
                    onClickBackChooseRole = { navController.popBackStack() }
                )
            }

            // Login Cliente
            composable("loginCliente") {
                LoginClienteScreen(
                    onSuccesfuloginCliente = {
                        val user = auth.currentUser
                        user?.let {
                            db.collection("usuarios").document(it.uid)
                                .set(mapOf("rol" to "cliente"))
                        }
                        navController.navigate("menuScreen") { popUpTo("startApp") { inclusive = false}}
                    },
                    onClickBackLoginCliente = { navController.popBackStack() }
                )
            }

            // Login Restaurante
            composable("loginRestaurante") {
                LoginRestauranteScreen(
                    onSuccesfuloginRestaurante = {
                        val user = auth.currentUser
                        user?.let {
                            db.collection("usuarios").document(it.uid)
                                .set(mapOf("rol" to "restaurante"))
                        }
                        navController.navigate("menuRestaurante") {popUpTo("startApp") { inclusive = false}}
                    },
                    onClickBackloginRestaurante = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("startApp") {
                            popUpTo("loginRestaurante") { inclusive = true}
                        }
                    }


                )
            }

            // Pantallas Cliente
            composable("menuScreen") { MenuScreen(navController) }
            composable("profileCliente") { ProfileClienteScreen(navController) }
            composable("mapaCliente") { MapaScreen(navController) }
            composable("chatCliente") { ChatScreen(navController) }
            composable("walletCliente") { ProductosScreen(navController) }
            composable("settingsCliente") {
                EncabezadoConfiguracion(
                    onClickBackConfig = {
                        navController.navigate("menuScreen") {
                            popUpTo("menuScreen") { inclusive = true }
                        }
                    }
                )
            }

            // Pantallas Restaurante
            composable("menuRestaurante") {
                MenuRestauranteScreen(
                    navController = navController,
                    onClickLogout = {
                        FirebaseAuth.getInstance().signOut()
                            navController.navigate("loginRestaurante") { // nombre correcto
                        popUpTo("menuRestaurante") { inclusive = true }
                        }
                    }// Cierra la sesión de Firebase



                )
            }

            composable("menuRestauranteScreen") { MenuRestauranteScreen(navController = navController) }
            composable("scanRestaurante") { QRScreen(navController = navController)}
            composable("profileRestaurante") { ClientsWaitingScreen(navController = navController)}
            composable("chatRestaurante") { ChatRestauranteScreen(navController = navController)}

            composable("editarMenu") {
                EditarMenuScreen(navController = navController)}
            composable("agregarProducto") {
                AgregarProductoScreen(navController = navController)
                }
            }
        }
    }


@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
    }
}