package co.edu.karolsaavedra.veezy.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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


@Composable
fun BurgerCard(burger: Burger) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F3FA))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = burger.imageRes),
                contentDescription = burger.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = burger.name,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF641717),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Restaurante: \n${burger.restaurant}",
                color = Color(0xFF641717),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)

            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Horario: \n${burger.hours}",
                color = Color(0xFF641717),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "${burger.price}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4A017),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End)
                    .padding(end = 16.dp)
            )
        }
    }
}