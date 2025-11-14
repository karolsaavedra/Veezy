package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

// ========== CARD PARA RESTAURANTE (con botón eliminar) ==========
@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Diálogo de confirmación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Eliminar producto",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF641717)
                )
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar \"${producto.nombreProducto}\"? Esta acción no se puede deshacer.",
                    color = Color(0xFF641717)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444))
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = Color(0xFF641717))
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Card(
        modifier = Modifier
            .aspectRatio(0.8f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBEAEA)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Imagen del producto
                AsyncImage(
                    model = producto.imagenUrl,
                    contentDescription = producto.nombreProducto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Información del producto
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFBEAEA))
                        .padding(12.dp)
                ) {
                    Text(
                        text = producto.nombreProducto,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF641717),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$ ${producto.precio}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4A017)
                    )
                }
            }

            // Botón de eliminar en la esquina superior derecha
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar producto",
                    tint = Color(0xFFFF4444),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ========== CARD PARA CLIENTE (sin botón eliminar) ==========
@Composable
fun ProductCardCliente(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F3FA))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Imagen del producto
            Image(
                painter = rememberAsyncImagePainter(model = producto.imagenUrl),
                contentDescription = producto.nombreProducto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            // Nombre del producto
            Text(
                text = producto.nombreProducto,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF641717),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            // Restaurante
            Text(
                text = "Restaurante: ${producto.nombreRestaurante}",
                color = Color(0xFF641717),
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            // Horario
            if (producto.horario.isNotEmpty()) {
                Text(
                    text = "${producto.horario}",
                    color = Color(0xFF641717),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )
            }

            // Precio
            Text(
                text = "$ ${producto.precio}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4A017),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.End)
                    .padding(end = 16.dp)
            )
        }
    }
}