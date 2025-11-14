package co.edu.karolsaavedra.veezy.menu

import android.util.Log
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBarRestaurante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MenuRestauranteScreen(
    navController: NavHostController,
    onClickLogout: () -> Unit = {}
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    var nombreRestaurante by remember { mutableStateOf("") }
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(true) {
        try {
            if (user != null) {
                // Obtener nombre del restaurante
                val restauranteDoc = db.collection("restaurantes").document(user.uid).get().await()
                nombreRestaurante = restauranteDoc.getString("nombreRestaurante") ?: "Mi Restaurante"

                // Escuchar cambios en tiempo real de los productos
                db.collection("restaurantes")
                    .document(user.uid)
                    .collection("productos")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e("MenuRestauranteScreen", "Error al escuchar productos: ${error.message}")
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val listaProductos = snapshot.documents.mapNotNull { doc ->
                                val producto = doc.toObject(Producto::class.java)
                                producto?.copy(
                                    id = doc.id,
                                    nombreRestaurante = nombreRestaurante
                                )
                            }
                            productos = listaProductos
                            Log.d("MenuRestauranteScreen", "Productos actualizados: ${productos.size}")
                        }
                        isLoading = false
                    }
            }
        } catch (e: Exception) {
            Log.e("MenuRestauranteScreen", "Error al cargar datos: ${e.message}")
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

                Text(
                    text = nombreRestaurante,
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

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productos) { producto ->
                        ProductoCard(
                            producto = producto,
                            onClick = {
                                // Navegar a editar producto con el ID
                                navController.navigate("editarProducto/${producto.id}")
                            },
                            onDelete = {
                                user?.let { usuario ->

                                    // 1. Primero eliminar la imagen del Storage (si existe)
                                    try {
                                        if (!producto.imagenUrl.isNullOrEmpty()) {
                                            val storageRef = com.google.firebase.storage.FirebaseStorage
                                                .getInstance()
                                                .getReferenceFromUrl(producto.imagenUrl)

                                            storageRef.delete()
                                                .addOnSuccessListener {
                                                    Log.d("MenuRestauranteScreen", "Imagen eliminada de Storage")
                                                }
                                                .addOnFailureListener { e ->
                                                    Log.e("MenuRestauranteScreen", "Error al eliminar imagen: ${e.message}")
                                                }
                                        }
                                    } catch (e: Exception) {
                                        Log.e("MenuRestauranteScreen", "URL de imagen inválida: ${e.message}")
                                    }

                                    // 2. Ahora eliminar el documento de Firestore
                                    db.collection("restaurantes")
                                        .document(usuario.uid)
                                        .collection("productos")
                                        .document(producto.id)
                                        .delete()
                                        .addOnSuccessListener {
                                            Log.d("MenuRestauranteScreen", "Producto eliminado: ${producto.id}")
                                        }
                                        .addOnFailureListener { error ->
                                            Log.e("MenuRestauranteScreen", "Error al eliminar: ${error.message}")
                                        }
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