package co.edu.karolsaavedra.veezy.ViewGeneral

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.edu.karolsaavedra.veezy.R

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(412.dp)
            .height(56.dp)
            .background(
                color = Color(0xFF641717),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icons: List<Int> = listOf(
            R.drawable.profile___iconly_pro,
            R.drawable.location___iconly_pro,
            R.drawable.home___iconly_pro,
            R.drawable.chat_2___iconly_pro,
            R.drawable.wallet___iconly_pro
        )
        icons.forEach { icon ->
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun BottomBar2(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(412.dp)
            .height(56.dp)
            .background(
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icons: List<Int> = listOf(
            R.drawable.profile___iconly_pro,
            R.drawable.location___iconly_pro,
            R.drawable.home___iconly_pro,
            R.drawable.chat_2___iconly_pro,
            R.drawable.wallet___iconly_pro
        )
        icons.forEach { icon ->
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color(0xFFCB6363))
            )
        }
    }
}
