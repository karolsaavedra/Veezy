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
import co.edu.karolsaavedra.veezy.ViewRestaurante.LoginRestauranteScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.RegisterRestaurante
import co.edu.karolsaavedra.veezy.menu.MenuRestauranteScreen
import co.edu.karolsaavedra.veezy.menu.MenuScreen

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
                onClickRegisterRestaurante = { myNavController.navigate("registerRestaurante") },
                onClickStartapp = { myNavController.navigate("chooseRole") }
            )
        }

        //  Registro cliente
        composable("registerCliente") {
            RegisterCliente(
                onSuccesfuRegisterCliente = { myNavController.navigate("loginCliente") {popUpTo(0) } },
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
                onSuccesfuloginCliente = { myNavController.navigate("menuCliente") },
                onClickBackLoginCliente = { myNavController.popBackStack() }
            )
        }

        //  Login Restaurante
        composable("loginRestaurante") {
            LoginRestauranteScreen(
                onSuccesfuloginRestaurante = { myNavController.navigate("firstScreenRestaurant"){popUpTo("loginRestaurante") {inclusive = true} }},
                onClickBackloginRestaurante = { myNavController.popBackStack() }
            )
        }

        //  Perfil Cliente
        composable("profileCliente") {
            ProfileClienteScreen(
                onClickLogout = { myNavController.popBackStack() }
            )
        }

        //  Configuración Cliente
        composable("settingsCliente") {
            EncabezadoConfiguracion(
                onClickBackConfig = { myNavController.popBackStack() }
            )
        }

        composable("menuCliente") {
            MenuScreen()
        }
        composable("registerRestaurante") {
            RegisterRestaurante(
                onSuccesfuRegisterCliente = { myNavController.navigate("loginRestaurante") {popUpTo(0) } },
                onClickBackRegisterRestaurante = { myNavController.popBackStack() }
            )
        }





    }
}
