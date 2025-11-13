package co.edu.karolsaavedra.veezy.menu

import android.util.Log
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar

@Composable
fun MenuScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val productos = remember { mutableStateListOf<Producto>() }
    var isLoading by remember { mutableStateOf(true) }

    //Carga todos los productos de TODAS las subcolecciones "productos" bajo "restaurantes"
    LaunchedEffect(true) {
        try {
            productos.clear()
            val restaurantesSnap = db.collection("restaurantes").get().await()

            for (restauranteDoc in restaurantesSnap.documents) {
                val nombreRestaurante = restauranteDoc.getString("nombreRestaurante") ?: ""
                val horario = restauranteDoc.getString("horario") ?: ""
                val imagenRestaurante = restauranteDoc.getString("imagenUrl") ?: ""
                val direccion = restauranteDoc.getString("direccion") ?: ""

                // Recorremos los productos dentro del restaurante
                val productosSnap = restauranteDoc.reference.collection("productos").get().await()
                for (productoDoc in productosSnap.documents) {
                    val producto = productoDoc.toObject(Producto::class.java)
                    if (producto != null) {
                        // Se agrega el id del documento del producto
                        val productoConDatos = producto.copy(
                            id = productoDoc.id,
                            nombreRestaurante = nombreRestaurante,
                            horario = horario,
                            imagenUrl = if (producto.imagenUrl.isNotEmpty()) producto.imagenUrl else imagenRestaurante,
                            direccion = direccion
                        )
                        productos.add(productoConDatos)
                    }
                }
            }

            Log.d("MenuScreen", "Productos cargados: ${productos.size}")
        } catch (e: Exception) {
            Log.e("MenuScreen", "Error al cargar productos: ${e.message}")
        } finally {
            isLoading = false
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

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else if (productos.isEmpty()) {
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
                        items(productos) { producto ->
                            ProductCardCliente(producto = producto, onClick = {
                                // Usa el id agregado para navegar correctamente
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