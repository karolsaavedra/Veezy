package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar2
import co.edu.karolsaavedra.veezy.ViewGeneral.BottomBar3

@Preview(showBackground = true)
@Composable
fun MenuRestauranteScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF641717)) // Fondo vino oscuro
    ) {
        // --- Aros decorativos (fondo) ---
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

        // --- Contenido principal ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // espacio para BottomBar2
        ) {
            // Header con ícono editar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 54.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color(0xFFF0F3FA),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Título
            Text(
                text = "Nombre restaurante",
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

            // Grid con productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(burgerList) { burger ->
                    BurgerCard(burger = burger)
                }
            }
        }

        // Bottom bar
        BottomBar3(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

