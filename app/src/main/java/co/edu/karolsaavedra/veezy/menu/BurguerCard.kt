package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R
import coil.compose.rememberAsyncImagePainter


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
            Text(
                text = ": ${producto.horario}",
                color = Color(0xFF641717),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            // Precio
            Text(
                text = producto.precio,
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



@Composable
fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(235.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F3FA))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = producto.imagenUrl),
                contentDescription = producto.nombreProducto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = producto.nombreProducto,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF641717),
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
            Text(
                text = producto.nombreRestaurante,
                color = Color(0xFF000000),
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            Text(
                text = producto.precio,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4A017),
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End)
                    .padding(end = 16.dp)
            )
        }
    }
}