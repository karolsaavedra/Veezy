package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar


@Composable
fun MenuScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFF641717),
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding() // evita que la barra quede muy abajo
                    .background(Color(0xFF641717))
            ) {
                BottomBar(navController = navController, isBackgroundWine = true)
            }
        }
    ){ padding ->
        // Aquí va tu contenido principal
        Box(
            modifier = Modifier
                .padding(padding) // evita que se tape el contenido
                .fillMaxSize()
                .background(Color(0xFF641717)) // Fondo vino oscuro
        ) {
            Image(
                painter = painterResource(id = R.drawable.tulio),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .alpha(0.3f)
                    .align(Alignment.TopCenter)
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 54.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navController.navigate("settingsCliente")
                            }
                    )
                }

                Text(
                    text = "Participantes",
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.W700,
                        color = Color(0xFFF0F3FA)
                    ),
                    modifier = Modifier
                        .width(253.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(100.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(burgerList) { burger ->
                        BurgerCard(burger, onClick = { navController.navigate("Burgerinfo") })
                    }



                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    val navController = rememberNavController()
    MenuScreen(navController = navController)
}

