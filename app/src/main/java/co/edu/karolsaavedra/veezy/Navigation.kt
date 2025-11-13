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
import co.edu.karolsaavedra.veezy.producto.InfoProducto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    val auth = remember { FirebaseAuth.getInstance() }
    val db = remember { FirebaseFirestore.getInstance() }

    // Se inicializa como null para saber cuándo mostrar el NavHost
    var startDestination by remember { mutableStateOf<String?>(null) }

    // Detecta si el usuario autenticado está en clientes o restaurantes
    LaunchedEffect(Unit) {
        val user = auth.currentUser
        if (user == null) {
            startDestination = "startApp"
            return@LaunchedEffect
        }

        val uid = user.uid
        val clientesRef = db.collection("clientes").document(uid)
        val restaurantesRef = db.collection("restaurantes").document(uid)

        clientesRef.get()
            .addOnSuccessListener { cliente ->
                if (cliente.exists()) {
                    startDestination = "menuScreen" // Cliente
                } else {
                    restaurantesRef.get()
                        .addOnSuccessListener { rest ->
                            startDestination = if (rest.exists()) {
                                "menuRestaurante" // Restaurante
                            } else {
                                "chooseRole"       // No tiene perfil aún
                            }
                        }
                        .addOnFailureListener {
                            startDestination = "chooseRole"
                        }
                }
            }
            .addOnFailureListener {
                startDestination = "chooseRole"
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
            // -------- Pantalla inicial --------
            composable("startApp") {
                StartScreen(
                    onClickRegisterCliente = { navController.navigate("registerCliente") },
                    onClickRegisterRestaurante = { navController.navigate("registerRestaurante") },
                    onClickStartapp = { navController.navigate("chooseRole") }
                )
            }

            // -------- Registro cliente --------
            composable("registerCliente") {
                RegisterCliente(
                    onSuccesfuRegisterCliente = {
                        // Va al home de cliente
                        navController.navigate("menuScreen") {
                            popUpTo("startApp") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onClickBackRegister = { navController.popBackStack() }
                )
            }

            // -------- Registro restaurante --------
            composable("registerRestaurante") {
                RegisterRestaurante(
                    onSuccesfuRegisterRestaurante = {
                        // Va al home de restaurante
                        navController.navigate("menuRestaurante") {
                            popUpTo("startApp") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onClickBackRegisterRestaurante = { navController.popBackStack() }
                )
            }

            // -------- Elección de rol --------
            composable("chooseRole") {
                ChooseRoleScreen(
                    onClickCliente = { navController.navigate("loginCliente") },
                    onClickRestaurante = { navController.navigate("loginRestaurante") },
                    onClickBackChooseRole = { navController.popBackStack() }
                )
            }

            // -------- Login Cliente --------
            composable("loginCliente") {
                LoginClienteScreen(
                    onSuccesfuloginCliente = {
                        // Si ya existe perfil en /clientes/{uid}, el startDestination lo resolverá en reinicios.
                        // Aquí simplemente navega al home de cliente.
                        navController.navigate("menuScreen") {
                            popUpTo("startApp") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onClickBackLoginCliente = {  FirebaseAuth.getInstance().signOut()
                        navController.navigate("startApp") {
                            popUpTo("loginCliente") { inclusive = true }
                            launchSingleTop = true}}
                )
            }

            // -------- Login Restaurante --------
            composable("loginRestaurante") {
                LoginRestauranteScreen(
                    onSuccesfuloginRestaurante = {
                        // Igual que cliente: navega al home de restaurante.
                        navController.navigate("menuRestaurante") {
                            popUpTo("startApp") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onClickBackloginRestaurante = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("startApp") {
                            popUpTo("loginRestaurante") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // ============== Pantallas Cliente ==============
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
                            launchSingleTop = true
                        }
                    }
                )
            }

            // ============== Pantallas Restaurante ==============
            composable("menuRestaurante") {
                MenuRestauranteScreen(
                    navController = navController,
                    onClickLogout = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("loginRestaurante") {
                            popUpTo("menuRestaurante") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // (si la necesitas como otra ruta aparte)
            composable("menuRestauranteScreen") { MenuRestauranteScreen(navController = navController) }
            composable("scanRestaurante") { QRScreen(navController = navController) }
            composable("profileRestaurante") { ClientsWaitingScreen(navController = navController) }
            composable("chatRestaurante") { ChatScreen(navController = navController) }
            composable("editarMenu") { EditarMenuScreen(navController = navController) }
            composable("agregarProducto") { AgregarProductoScreen(navController = navController) }

            composable("Burgerinfo/{restauranteNombre}") { backStackEntry ->
                val restauranteNombre = backStackEntry.arguments?.getString("restauranteNombre") ?: ""
                InfoProducto(navController = navController, restauranteNombre = restauranteNombre)
            }

            composable("ReservarTurnoRestaurante/{restauranteNombre}") { backStackEntry ->
                val restauranteNombre = backStackEntry.arguments?.getString("restauranteNombre") ?: "Desconocido"
                PaginaReservas(
                    navController = navController,
                    onClickParaLlevar = { navController.navigate("ReservarTurnoParallevar") },
                    //pasa el nombre del restaurante
                    nombreRestaurante = restauranteNombre
                )
            }
            composable("ReservarTurnoParallevar") {
                PaginaReservasParallevar(navController = navController,
                onClickParaRestaurante = { navController.navigate("ReservarTurnoRestaurante") },
                onClickParaLlevar = { navController.navigate("ReservarTurnoParallevar") }

                )
            }


            composable("TurnoGenerado") { TurnoScreen(navController = navController) }

            // ============== Pantallas Chat ==============
            composable("chat_list") {
                ChatListScreen(navController)
            }

            composable("chat_detail/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatDetailScreen(navController = navController, chatId = chatId)
            }
             composable("TurnoPendiente") {
                TurnoPendienteScreen(navController = navController)
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Cargando…")
        }
    }
}
