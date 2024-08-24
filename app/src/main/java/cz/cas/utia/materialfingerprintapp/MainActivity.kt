package cz.cas.utia.materialfingerprintapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.cas.utia.materialfingerprintapp.core.navigation.MainNavigation
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.AppCustomTheme
import cz.cas.utia.materialfingerprintapp.core.ui.theme.original.MaterialFingerprintAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //todo nechat? je to tu automaticky
        setContent {
            AppCustomTheme {
                MainNavigation()

            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialFingerprintAppTheme {
        Greeting("Android")
    }
}