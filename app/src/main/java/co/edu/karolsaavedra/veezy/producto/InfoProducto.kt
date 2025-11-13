package co.edu.karolsaavedra.veezy.producto

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.menu.BurgerInfo
import co.edu.karolsaavedra.veezy.menu.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun InfoProducto(navController: NavController, restauranteNombre: String, productoId: String) {

    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isFavorite by remember { mutableStateOf(false) }
    var producto by remember { mutableStateOf<Producto?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var ubicacionRestaurante = LatLng(7.108988715896521, -73.10634914047473)

    // Cargar producto desde Firebase
    LaunchedEffect(productoId) {
        try {
            val docs = db.collection("restaurantes")
                .whereEqualTo("nombreRestaurante", restauranteNombre)
                .get()
                .await()

            if (!docs.isEmpty) {
                val restauranteDoc = docs.documents.first()
                val productoDoc = restauranteDoc.reference
                    .collection("productos")
                    .document(productoId)
                    .get()
                    .await()

                if (productoDoc.exists()) {
                    producto = productoDoc.toObject(Producto::class.java)
                } else {
                    println("No se encontró producto con ID: $productoId")
                }
            } else {
                println("No se encontró restaurante con nombre: $restauranteNombre")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    // Cargar estado de favorito desde Firebase
    LaunchedEffect(productoId, userId) {
        if (userId == null) {
            println("Usuario no autenticado")
            return@LaunchedEffect
        }

        try {
            val snap = db.collection("clientes")
                .document(userId)
                .collection("favoritos")
                .document(productoId)
                .get()
                .await()

            isFavorite = snap.exists()
            println("Favorito cargado: $isFavorite para producto: $productoId")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al cargar favorito: ${e.message}")
        }
    }

    // Función para guardar/eliminar favorito
    fun toggleFavorito() {
        if (userId == null) return

        val nuevoEstado = !isFavorite
        isFavorite = nuevoEstado // Actualizar UI inmediatamente

        scope.launch {
            try {
                val favoritoRef = db.collection("usuarios")
                    .document(userId)
                    .collection("favoritos")
                    .document(productoId)

                if (nuevoEstado) {
                    // Agregar a favoritos
                    favoritoRef.set(
                        mapOf(
                            "productoId" to productoId,
                            "nombreProducto" to (producto?.nombreProducto ?: ""),
                            "restauranteNombre" to restauranteNombre,
                            "timestamp" to System.currentTimeMillis()
                        )
                    ).await()
                    println("Producto agregado a favoritos")
                } else {
                    // Eliminar de favoritos
                    favoritoRef.delete().await()
                    println("Producto eliminado de favoritos")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error al guardar favorito: ${e.message}")
                isFavorite = !nuevoEstado // Revertir si falla
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF641717))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 54.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { navController.popBackStack() }
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Volver",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { toggleFavorito() }
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {

                }
            }

            // Mostrar datos del producto
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFFCC00))
                }
            } else {
                producto?.let {
                    BurgerInfo(producto = it, productoId = productoId)
                } ?: Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Producto no encontrado",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // Botones inferiores
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigate("ReservarTurnoRestaurante/${restauranteNombre}")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC00)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .height(55.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Turno",
                    color = Color(0xFF641717),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Card(
                modifier = Modifier.size(55.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCC00)),
                onClick = {
                    val url = Uri.parse("google.navigation:q=${ubicacionRestaurante.latitude},${ubicacionRestaurante.longitude}&mode=d")
                    val intent = Intent(Intent.ACTION_VIEW, url)
                    intent.setPackage("com.google.android.apps.maps")
                    context.startActivity(intent)
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Ubicación",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfopProductopreview() {
    val navController = rememberNavController()
    InfoProducto(navController = navController, restauranteNombre = "Mi Restaurante", productoId = "demo123")
}