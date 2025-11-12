package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.karolsaavedra.veezy.R

@Composable
fun BurgerInfo(burger: Burger) {
    // Estado para controlar cuántas hamburguesas (calificación) están activas
    var rating by remember { mutableStateOf(0) }

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

            // Imagen principal de la hamburguesa
            Image(
                painter = painterResource(R.drawable.burger1),
                contentDescription = burger.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(0.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                text = burger.name,
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 5.dp)
            )

            Spacer(modifier = Modifier.height(57.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    for (i in 1..5) {
                        Image(
                            painter = painterResource(
                                id = if (i <= rating) R.drawable.burger_yellow else R.drawable.burger_normal
                            ),
                            contentDescription = "Rating $i",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(end = 4.dp)
                                .clickable { rating = i }
                        )
                    }
                }

                Text(
                    text = burger.price,
                    color = Color(0xFFD4A017),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Descripción ---
            Text(
                text = "Desde el downtown de Brooklyn… delicioso pan brioche con una corteza de miel de maple, doble carne tipo Oklahoma con doble queso cheddar importado, un picadillo de sweet panceta ahumado en nogal, papa golden string coronada con una deliciosa salsa creada exclusivamente para esta burger, rodaja de tomate rojo, cogollo europeo y pepinillo tipo kosher. Don’t stress, feel fresco.",
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            )
        }
    }
}
