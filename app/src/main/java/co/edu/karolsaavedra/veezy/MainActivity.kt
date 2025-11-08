package co.edu.karolsaavedra.veezy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import co.edu.karolsaavedra.veezy.ViewCliente.TurnoScreen
import co.edu.karolsaavedra.veezy.ViewRestaurante.QRScreen
import co.edu.karolsaavedra.veezy.producto.InfoProducto
import co.edu.karolsaavedra.veezy.ui.theme.VeezyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VeezyTheme {
                NavigationApp()

            }
        }
    }
}

