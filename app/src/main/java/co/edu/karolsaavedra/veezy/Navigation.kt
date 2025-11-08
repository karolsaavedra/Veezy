package co.edu.karolsaavedra.veezy

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.ViewCliente.ChatScreen
import co.edu.karolsaavedra.veezy.ViewCliente.EncabezadoConfiguracion
import co.edu.karolsaavedra.veezy.ViewCliente.LoginClienteScreen
import co.edu.karolsaavedra.veezy.ViewCliente.MapaScreen
import co.edu.karolsaavedra.veezy.ViewCliente.ProductosScreen
import co.edu.karolsaavedra.veezy.ViewCliente.ProfileClienteScreen
import co.edu.karolsaavedra.veezy.ViewCliente.RegisterCliente
import co.edu.karolsaavedra.veezy.ViewGeneral.ChooseRoleScreen
import co.edu.karolsaavedra.veezy.ViewGeneral.StartScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.ClientsWaitingScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.LoginRestauranteScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.QRScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.RegisterRestaurante
import co.edu.karolsaavedra.veezy.menu.MenuRestauranteScreen
import co.edu.karolsaavedra.veezy.menu.MenuScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun NavigationApp() {


    val myNavController = rememberNavController()




    NavHost(
        navController = myNavController,
        startDestination = "startApp"
    ) {
        //  Pantalla inicial
        composable("startApp") {
            StartScreen(
                onClickRegisterCliente = { myNavController.navigate("registerCliente") },
                onClickRegisterRestaurante = {
                    Log.i("login", "registerRestaurante")
                    myNavController.navigate("registerRestaurante") },
                onClickStartapp = {
                    Log.i("login", "chooseRole")
                    myNavController.navigate("chooseRole") }

            )
        }

        //  Registro cliente
        composable("registerCliente") {
            RegisterCliente(
                onSuccesfuRegisterCliente = { myNavController.navigate("loginCliente") { popUpTo(0) } },
                onClickBackRegister = { myNavController.popBackStack() }
            )
        }

        //  Elección de rol
        composable("chooseRole") {
            ChooseRoleScreen(
                onClickCliente = {
                    Log.i("login", "loginCliente")
                    myNavController.navigate("loginCliente") },
                onClickRestaurante = {
                    Log.i("login", "loginRestaurante")

                    myNavController.navigate("loginRestaurante") },
                onClickBackChooseRole = { myNavController.popBackStack() }
            )
        }

        //  Login Cliente
        composable("loginCliente") {
            LoginClienteScreen(
                onSuccesfuloginCliente = {
                    myNavController.navigate("menuScreen")
                },
                onClickBackLoginCliente = {
                    myNavController.popBackStack()
                }
            )
        }

        //  Login Restaurante
        composable("loginRestaurante") {
            LoginRestauranteScreen(
                onSuccesfuloginRestaurante = {
                    Log.i("login", "menuRestaurante")

                    myNavController.navigate("menuRestaurante") {popUpTo("loginRestaurante") { inclusive = true }}
                },
                onClickBackloginRestaurante = { myNavController.popBackStack() }
            )
        }

        //  Perfil Cliente
        composable("profileCliente") {
            ProfileClienteScreen(
                navController = myNavController,
                onClickLogout = {
                    myNavController.navigate("loginCliente") {
                        popUpTo("menuScreen") { inclusive = true } // limpia la pila hasta el menú
                    }
                }
            )
        }

        // Configuración Cliente
        composable("settingsCliente") {
            EncabezadoConfiguracion(
                onClickBackConfig = {
                    myNavController.navigate("menuScreen") {
                        popUpTo("menuScreen") { inclusive = true }
                    }
                }
            )
        }


        //  Pantalla 1 (productos / menú)
        composable("menuScreen") {
            MenuScreen(navController = myNavController)
        }

        //  Registro restaurante
        composable("registerRestaurante") {
            RegisterRestaurante(
                onSuccesfuRegisterCliente = { myNavController.navigate("loginRestaurante") { popUpTo(0) } },
                onClickBackRegisterRestaurante = { myNavController.popBackStack() }
            )
        }

        //  Alias / rutas adicionales
        composable(route = "pantalla1") { MenuScreen(myNavController) }
        composable(route = "profileCliente") { ProfileClienteScreen(myNavController) }
        composable(route = "mapaCliente") { MapaScreen(myNavController) }
        composable(route = "chatCliente") { ChatScreen(myNavController) }
        composable(route = "walletCliente") { ProductosScreen(myNavController) }

        composable("chat") {
            ChatScreen(navController = myNavController)
        }

        composable("clientsWaiting") {
            ClientsWaitingScreen(navController = myNavController)
        }

        composable("qrScreen") {
            QRScreen(navController = myNavController)
        }

        composable("menuRestaurante") {
            MenuRestauranteScreen(navController = myNavController)
        }

        composable(route = "menuRestauranteScreen") { MenuRestauranteScreen(myNavController) }

        /* composable(route = "profileCliente") { ProfileClienteScreen(myNavController) }
        composable(route = "mapaCliente") { MapaScreen(myNavController) }
        composable(route = "chatCliente") { ChatScreen(myNavController) }
        composable(route = "walletCliente") { ProductosScreen(myNavController) } */
    }
}
