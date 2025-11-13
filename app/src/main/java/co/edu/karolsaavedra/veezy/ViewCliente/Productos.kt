package co.edu.karolsaavedra.veezy.ViewCliente

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class TurnoData(
    val id: String,
    val numero: Long,
    val restauranteNombre: String,
    val tipo: String,
    val estado: String,
    val timestamp: com.google.firebase.Timestamp
)

@Composable
fun ProductosScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val context = LocalContext.current

    var listaTurnos by remember { mutableStateOf<List<TurnoData>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("turnos")
                .whereEqualTo("clienteId", userId)
                .whereEqualTo("estado", "pendiente")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        cargando = false
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        listaTurnos = snapshot.documents.mapNotNull { doc ->
                            try {
                                TurnoData(
                                    id = doc.id,
                                    numero = doc.getLong("numero") ?: 0,
                                    restauranteNombre = doc.getString("restauranteNombre") ?: "Desconocido",
                                    tipo = doc.getString("tipo") ?: "N/A",
                                    estado = doc.getString("estado") ?: "pendiente",
                                    timestamp = doc.getTimestamp("timestamp") ?: com.google.firebase.Timestamp.now()
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }.sortedByDescending { it.timestamp.toDate().time }
                        cargando = false
                    }
                }
        }
    }

    Scaffold(
        containerColor = Color(0xFF641717)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    .fillMaxWidth()
                    .height(720.dp)
                    .offset(y = 0.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mis Turnos",
                        style = TextStyle(
                            fontSize = 40.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF863939)
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {



                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (cargando) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cargando turnos...",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color(0xFF641717)
                            )
                        )
                    }
                } else if (listaTurnos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No tienes turnos activos",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF641717)
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(listaTurnos, key = { it.id }) { turno ->
                            SwipeToDeleteCard(
                                turno = turno,
                                navController = navController,
                                onEliminar = {
                                    db.collection("turnos")
                                        .document(turno.id)
                                        .delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Turno eliminado correctamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                context,
                                                "Error al eliminar turno: ${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menú",
                    modifier = Modifier.size(32.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.bell),
                    contentDescription = "Notificaciones",
                    modifier = Modifier.size(32.dp)
                )
            }

            BottomBar(
                navController = navController,
                isBackgroundWine = false,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun SwipeToDeleteCard(
    turno: TurnoData,
    navController: NavController,
    onEliminar: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val swipeThreshold = -200f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        // Fondo rojo con icono de eliminar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFF4444), shape = RoundedCornerShape(20.dp))
                .padding(end = 30.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Eliminar",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        // Tarjeta deslizable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .background(Color(0xFF641717), shape = RoundedCornerShape(20.dp))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                if (offsetX.value < swipeThreshold) {
                                    // Eliminar
                                    offsetX.animateTo(-1000f, animationSpec = tween(300))
                                    onEliminar()
                                } else {
                                    // Volver a posición original
                                    offsetX.animateTo(0f, animationSpec = tween(300))
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newValue = (offsetX.value + dragAmount).coerceIn(-400f, 0f)
                                offsetX.snapTo(newValue)
                            }
                        }
                    )
                }
                .clickable(enabled = offsetX.value == 0f) {
                    navController.navigate("TurnoGenerado")
                }
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Información del turno (más compacta y a la izquierda)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Turno #${turno.numero}",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFC64F)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = turno.restauranteNombre,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = turno.tipo,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.afacad)),
                            color = Color(0xFFE0E0E0)
                        )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // QR más grande y prominente
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Barcode(
                        value = "${turno.id}-${turno.numero}",
                        type = BarcodeType.QR_CODE,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}