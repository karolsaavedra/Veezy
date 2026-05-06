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
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel // <-- Importante para usar viewModel()
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import co.edu.karolsaavedra.veezy.di.AppContainer
import co.edu.karolsaavedra.veezy.di.ViewModelFactory
import co.edu.karolsaavedra.veezy.presentation.menu.MenuViewModel


// TODO: Asegúrate de importar aquí tus clases AppContainer, ViewModelFactory y MenuViewModel
// import co.edu.karolsaavedra.veezy.di.AppContainer
// import co.edu.karolsaavedra.veezy.di.ViewModelFactory

@Composable
fun MenuScreen(navController: NavController) {
    // 1. Inyectamos el ViewModel usando tu Factory y el AppContainer
    val viewModel: MenuViewModel = viewModel(
        factory = ViewModelFactory { AppContainer.provideMenuViewModel() }
    )

    // 2. Observamos el estado de la UI de forma reactiva
    val uiState by viewModel.uiState.collectAsState()

    // 3. Cargamos los productos solo una vez al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarTodosLosProductos()
    }

    Scaffold(
        containerColor = Color(0xFF641717),
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .background(Color(0xFF641717))
            ) {
                BottomBar(navController = navController, isBackgroundWine = true)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF641717))
        ) {
            // Fondo decorativo
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

            // Contenido principal
            Column(modifier = Modifier.fillMaxSize()) {
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
                            .size(32.dp)
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

                // 4. Usamos el uiState para decidir qué mostrar
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else if (uiState.productos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay productos disponibles.",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.productos) { producto ->
                            ProductCardCliente(producto = producto, onClick = {
                                navController.navigate("Burgerinfo/${producto.nombreRestaurante}/${producto.id}")
                            })
                        }
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