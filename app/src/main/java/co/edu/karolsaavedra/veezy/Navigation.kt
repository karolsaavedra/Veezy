package co.edu.karolsaavedra.veezy

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable

fun NavigationApp(){
    val myNavController = rememberNavController()
    var myStartDestination: String= "start"



    NavHost(
        navController = myNavController,
        startDestination = "start"
    ) {
        // Pantalla de inicio
        composable("start") {
            StartScreen(
                onClickStartapp = {
                    myNavController.navigate("choose_role")
                },
                onSuccesfulstart = {
                    myNavController.navigate("home_cliente") {
                        popUpTo("start") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla para elegir tipo de usuario
        composable("choose_role") {
            ChooseRoleScreen(
                onClickCliente = {
                    myNavController.navigate("login_cliente")
                },
                onClickRestaurante = {
                    myNavController.navigate("login_restaurante")
                }
            )
        }

        // Login Cliente
        composable("login_cliente") {
            LoginClienteScreen(
                onSuccesfuloginCliente = {
                    myNavController.navigate("home_cliente") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                onClickRegisterCliente = {
                    // Aquí podrías ir a una pantalla de registro si la agregas
                }
            )
        }
/*
        // Login Restaurante
        composable("login_restaurante") {
            LoginRestauranteScreen(
                onSuccesfulogin = {
                    myNavController.navigate("home_restaurante") {
                        popUpTo("start") { inclusive = true }
                    }
                },
                onClickRegister = {
                    // Igual que arriba, si añades un registro
                }
            )
        }

        // Home Cliente
        composable("home_cliente") {
            HomeClienteScreen()
        }

        // Home Restaurante
        composable("home_restaurante") {
            HomeRestauranteScreen()
        }
    }

 */
}
}