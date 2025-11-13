package co.edu.karolsaavedra.veezy.ViewCliente

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.util.*

data class Restaurante(val nombreRestaurante: String, val direccion: String, val latLng: LatLng?)

@Composable
fun MapaScreen(navController: NavController) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()
    var restaurantes by remember { mutableStateOf<List<Restaurante>>(emptyList()) }

    // ======= CARGAR RESTAURANTES DESDE FIRESTORE =======
    LaunchedEffect(Unit) {
        firestore.collection("restaurantes").get()
            .addOnSuccessListener { result ->
                val tempList = mutableListOf<Restaurante>()
                for (doc in result) {
                    val nombre = doc.getString("nombreRestaurante") ?: ""
                    val direccion = doc.getString("direccion") ?: ""
                    val latLng = obtenerCoordenadas(context, direccion)
                    tempList.add(Restaurante(nombre, direccion, latLng))
                }
                restaurantes = tempList
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(7.1193, -73.1227), 12f) // Bucaramanga por defecto
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
            // ===== CÍRCULOS DECORATIVOS DEL FONDO =====
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

            // ===== PANEL BLANCO PRINCIPAL =====
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

                // ===== TÍTULO =====
                Text(
                    text = "Hamburguesas",
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF863939)
                    )
                )

                // ===== SUBTÍTULO =====
                Text(
                    text = "cerca a ti",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.afacad)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ===== MAPA =====
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .background(
                            color = Color(0xFFE5E5E5),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        restaurantes.forEach { restaurante ->
                            restaurante.latLng?.let { coords ->
                                Marker(
                                    state = rememberMarkerState(position = coords),
                                    title = restaurante.nombreRestaurante,
                                    snippet = restaurante.direccion
                                )
                            }
                        }
                    }
                }
            }

            // ===== ÍCONOS DE MENÚ Y CAMPANITA =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
            }

            // ===== BARRA INFERIOR =====
            BottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                navController = navController,
                isBackgroundWine = false
            )
        }
    }
}

// ======= FUNCIÓN PARA CONVERTIR DIRECCIÓN A COORDENADAS =======
fun obtenerCoordenadas(context: Context, direccion: String): LatLng? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val results = geocoder.getFromLocationName(direccion, 1)
        if (!results.isNullOrEmpty()) {
            LatLng(results[0].latitude, results[0].longitude)
        } else null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
