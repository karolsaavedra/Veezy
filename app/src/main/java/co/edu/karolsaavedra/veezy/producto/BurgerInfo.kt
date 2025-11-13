package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import co.edu.karolsaavedra.veezy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun BurgerInfo(producto: Producto, productoId: String) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val scope = rememberCoroutineScope()

    var rating by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar rating guardado desde Firebase - USANDO CLIENTES
    LaunchedEffect(productoId, userId) {
        if (userId == null) {
            println("Usuario no autenticado")
            isLoading = false
            return@LaunchedEffect
        }

        try {
            val snap = db.collection("clientes")
                .document(userId)
                .collection("preferencias")
                .document(productoId)
                .get()
                .await()

            if (snap.exists()) {
                rating = snap.getLong("rating")?.toInt() ?: 0
                println("Rating cargado: $rating para producto: $productoId")
            } else {
                println("No hay rating guardado para producto: $productoId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al cargar rating: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // Función para guardar el rating - USANDO CLIENTES
    fun guardarRating(newRating: Int) {
        if (userId == null) {
            println("Usuario no autenticado, no se puede guardar rating")
            return
        }

        rating = newRating // Actualizar UI inmediatamente

        scope.launch {
            try {
                db.collection("clientes")
                    .document(userId)
                    .collection("preferencias")
                    .document(productoId)
                    .set(
                        mapOf(
                            "rating" to newRating,
                            "nombreProducto" to producto.nombreProducto,
                            "productoId" to productoId,
                            "timestamp" to System.currentTimeMillis()
                        ),
                        com.google.firebase.firestore.SetOptions.merge()
                    )
                    .await()

                println("Rating guardado: $newRating para producto: $productoId en clientes/$userId/preferencias")
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error al guardar rating: ${e.message}")
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF641717))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF641717))
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen
            Image(
                painter = rememberAsyncImagePainter(model = producto.imagenUrl.ifEmpty { R.drawable.burger1 }),
                contentDescription = producto.nombreProducto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(0.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                text = producto.nombreProducto,
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Rating (hamburguesas) + precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    for (i in 1..5) {
                        Image(
                            painter = androidx.compose.ui.res.painterResource(
                                id = if (i <= rating) R.drawable.burger_yellow else R.drawable.burger_normal
                            ),
                            contentDescription = "Rating $i",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 4.dp)
                                .clickable {
                                    guardarRating(i)
                                }
                        )
                    }
                }

                Text(
                    text = producto.precio,
                    color = Color(0xFFD4A017),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = producto.descripcion.ifEmpty { "Delicioso producto disponible en nuestro menú." },
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
        }
    }
}