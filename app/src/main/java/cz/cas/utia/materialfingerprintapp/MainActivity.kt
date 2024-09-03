package cz.cas.utia.materialfingerprintapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.cas.utia.materialfingerprintapp.core.navigation.MainNavigation
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.CustomAppTheme
import cz.cas.utia.materialfingerprintapp.core.ui.theme.original.MaterialFingerprintAppTheme
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.PermissionsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //todo nechat? je to tu automaticky

        //val intent = Intent(this, Camera::class.java)
        //startActivity(intent)
        setContent {
            CustomAppTheme {
                MainNavigation()
                //PermissionsScreen()

            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialFingerprintAppTheme {
        Greeting("Android")
    }
}