package co.edu.karolsaavedra.veezy.ViewRestaurante

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun FirstPageRestaurante(
    onClickEdit: () -> Unit = {},
    OnclickRegisterStartapp: () -> Unit = {}


) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF641717))
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .width(356.dp)
                    .height(356.dp)
            ) {

                Box(

                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    //  Aro superior izquierdo (más grande)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = (-180).dp, y = (-150).dp)
                            .border(
                                width = 3.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    //Aro inferior derecho (pequeño)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .offset(x = (180).dp, y = (30).dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )
                    // Aro inferior izquierdo (pequeño)
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .offset(x = (-170).dp, y = (-155).dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )

                    // Aro inferior derecho (más grande)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .offset(x = (177).dp, y = (20).dp)
                            .border(
                                width = 3.dp,
                                color = Color(0xFFA979A7),
                                shape = CircleShape
                            )
                    )
                }


            }
            Spacer(modifier = Modifier.height(-100.dp))
            Text(
                text = "Nombre restaurante"
            )
            }

        }
}