package co.edu.karolsaavedra.veezy.ViewGeneral

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    isBackgroundWine: Boolean = true // Indica si el fondo actual es vino tinto
) {
    // Colores dinámicos según el fondo
    val barColor = if (isBackgroundWine) Color.White else Color(0xFF641717)
    val iconColor = if (isBackgroundWine) Color(0xFF641717) else Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = barColor,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val items = listOf(
            Pair(R.drawable.profile___iconly_pro, "profileCliente"),
            Pair(R.drawable.location___iconly_pro, "mapaCliente"),
            Pair(R.drawable.home___iconly_pro, "menuScreen"),
            Pair(R.drawable.chat_2___iconly_pro, "chatCliente"),
            Pair(R.drawable.wallet___iconly_pro, "walletCliente")
        )

        items.forEach { (icon, route) ->
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.navigate(route) {
                            popUpTo("menuScreen") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(iconColor) // Cambia el color de los íconos
            )
        }
    }
}







@Composable
fun BottomBarRestaurante(
    modifier: Modifier = Modifier,
    navController: NavController,
    isBackgroundWine: Boolean = true // Indica si el fondo actual es vino tinto
) {
    // Colores dinámicos según el fondo
    val barColor = if (isBackgroundWine) Color.White else Color(0xFF641717)
    val iconColor = if (isBackgroundWine) Color(0xFF641717) else Color.White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = barColor,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val items = listOf(
            Pair(R.drawable.profile___iconly_pro, "profileRestaurante"),
            Pair(R.drawable.scan___iconly_pro, "scanRestaurante"),
            Pair(R.drawable.home___iconly_pro, "menuRestauranteScreen"),
            Pair(R.drawable.chat_2___iconly_pro, "chatRestaurante"),
        )

        items.forEach { (icon, route) ->
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.navigate(route) {
                            popUpTo("menuRestaurante") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(iconColor) // Cambia el color de los íconos
            )
        }
    }
}
