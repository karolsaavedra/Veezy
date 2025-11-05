package co.edu.karolsaavedra.veezy

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.ViewCliente.EncabezadoConfiguracion
import co.edu.karolsaavedra.veezy.ViewCliente.LoginClienteScreen
import co.edu.karolsaavedra.veezy.ViewCliente.ProfileClienteScreen
import co.edu.karolsaavedra.veezy.ViewCliente.RegisterCliente
import co.edu.karolsaavedra.veezy.ViewGeneral.ChooseRoleScreen
import co.edu.karolsaavedra.veezy.ViewGeneral.StartScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.ClinetsWaitingScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.FirstPageRestaurante
import co.edu.karolsaavedra.veezy.ViewRestaurante.LoginRestauranteScreen

@Composable

fun NavigationApp(){
    val myNavController = rememberNavController()
    var myStartDestination: String= "start"



    NavHost(
        navController =  myNavController,
        startDestination = "startApp"
    ) {

        //  Pantalla inicial
        composable("startApp") {
            StartScreen(
                onClickRegisterCliente = {myNavController.navigate("registerCliente") },
                onClickStartapp = { myNavController.navigate("chooseRole") }
            )
        }

        //  Registro
        composable("registerCliente") {
            RegisterCliente(
                onSuccesfuRegisterCliente = { myNavController.navigate("loginCliente") },
                onClickBackRegister = { myNavController.popBackStack() }
            )
        }

        //  Elección de rol
        composable("chooseRole") {
            ChooseRoleScreen(
                onClickCliente = { myNavController.navigate("loginCliente") },
                onClickRestaurante = { myNavController.navigate("loginRestaurante") }
            )
        }

        //  Login Cliente
        composable("loginCliente") {
            LoginClienteScreen(
                onSuccesfuloginCliente = { myNavController.navigate("mainCliente") },
                onClickBackLoginCliente = { myNavController.popBackStack() }
            )
        }

        //  Login Restaurante
        composable("loginRestaurante") {
            LoginRestauranteScreen(
                onSuccesfuloginRestaurante = { myNavController.navigate("firstScreenRestaurant") },
                onClickBackloginRestaurante = { myNavController.popBackStack() }
            )
        }
/*
        //  Pantalla principal cliente
        composable("mainCliente") {
            Productos(
                onProfileClick = { myNavController.navigate("profileCliente") },
                onSettingsClick = { myNavController.navigate("settingsCliente") }
            )
        }
*/
        //  Perfil Cliente
        composable("profileCliente") {
            ProfileClienteScreen(
                onClickCerrarSesion = { myNavController.popBackStack() }
            )
        }

        //  Configuración Cliente
        composable("settingsCliente") {
            EncabezadoConfiguracion(
                onClickBackConfig = { myNavController.popBackStack() }
            )
        }
/*
        //  Editar menú
        composable("editarMenu") {
            EditarMenuScreen(
                onNextClick = { myNavController.navigate("confirmarMenu") },
                onBack = { myNavController.popBackStack() }
            )
        }

        // Confirmar menú (esta se borra al confirmar)
        composable("confirmarMenu") {
            ConfirmarMenuScreen(
                onConfirmClick = {
                    // Navega a la primera pantalla restaurante y borra la pila previa
                    myNavController.navigate("firstScreenRestaurant") {
                        popUpTo("editarMenu") { inclusive = true } // borra las pantallas anteriores
                    }
                }
            )
        }
*/
    }
}


