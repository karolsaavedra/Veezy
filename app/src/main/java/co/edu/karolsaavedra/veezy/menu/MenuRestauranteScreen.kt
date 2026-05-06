package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.google.firebase.auth.FirebaseAuth
import co.edu.karolsaavedra.veezy.di.AppContainer
import co.edu.karolsaavedra.veezy.di.ViewModelFactory
import co.edu.karolsaavedra.veezy.presentation.menu.MenuViewModel
import co.edu.karolsaavedra.veezy.presentation.restaurante.RestauranteViewModel // <-- Nuevo import

@Composable
fun MenuRestauranteScreen(
    navController: NavHostController,
    onClickLogout: () -> Unit = {}
) {
    // 1. Inyectamos ambos ViewModels
    val menuViewModel: MenuViewModel = viewModel(
        factory = ViewModelFactory { AppContainer.provideMenuViewModel() }
    )
    val restauranteViewModel: RestauranteViewModel = viewModel(
        factory = ViewModelFactory { AppContainer.provideRestauranteViewModel() }
    )

    // 2. Observamos ambos estados
    val uiState by menuViewModel.uiState.collectAsState()
    val restauranteState by restauranteViewModel.uiState.collectAsState()

    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid ?: ""

    // 3. Iniciamos la observación de productos y cargamos los datos del restaurante
    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            menuViewModel.observarProductosRestaurante(uid)
            restauranteViewModel.cargarRestaurante(uid) // <-- Carga el nombre y datos del perfil
        }
    }

    Scaffold(
        containerColor = Color(0xFF641717),
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .background(Color(0xFF641717))
            ) {
                BottomBarRestaurante(navController = navController, isBackgroundWine = true)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF641717))
        ) {
            Image(
                painter = painterResource(id = R.drawable.group_3),
                contentDescription = "Círculo superior izquierdo",
                modifier = Modifier
                    .width(110.dp)
                    .height(250.dp)
                    .align(Alignment.TopStart),
                contentScale = ContentScale.None
            )

            Image(
                painter = painterResource(id = R.drawable.group_5),
                contentDescription = "Círculo superior derecho",
                modifier = Modifier
                    .width(110.dp)
                    .height(65.dp)
                    .align(Alignment.TopEnd),
                contentScale = ContentScale.None
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 54.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("loginRestaurante") {
                                popUpTo("menuRestauranteScreen") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD99C00)
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
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
                }

                // 4. Usamos el estado del RestauranteViewModel para el nombre
                Text(
                    text = restauranteState.restaurante?.nombreRestaurante ?: "Mi Restaurante",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF0F3FA)
                    ),
                    modifier = Modifier.padding(start = 28.dp)
                )
                Text(
                    text = "¿Listo para renovar tu menú?",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFFF0F3FA).copy(alpha = 0.8f)
                    ),
                    modifier = Modifier
                        .padding(start = 28.dp, top = 4.dp, bottom = 20.dp)
                )

                // 5. Manejo del estado de carga y lista de productos (usa menuViewModel state)
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.productos) { producto ->
                            ProductoCard(
                                producto = producto,
                                onClick = {
                                    navController.navigate("editarProducto/${producto.id}")
                                },
                                onDelete = {
                                    // 6. Delegamos la eliminación al MenuViewModel
                                    if (uid.isNotEmpty()) {
                                        menuViewModel.eliminarProducto(uid, producto.id, producto.imagenUrl)
                                    }
                                }
                            )
                        }

                        item {
                            AddBurgerButton(onClick = {
                                navController.navigate("agregarProducto")
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddBurgerButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFBEAEA))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFE6B0AA), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar nuevo producto",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuRestauranteScreenPreview() {
    val navController = rememberNavController()
    MenuRestauranteScreen(navController = navController)
}