package co.edu.karolsaavedra.veezy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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