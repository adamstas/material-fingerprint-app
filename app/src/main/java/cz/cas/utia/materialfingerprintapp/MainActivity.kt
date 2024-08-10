package cz.cas.utia.materialfingerprintapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import cz.cas.utia.materialfingerprintapp.ui.theme.MaterialFingerprintAppTheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //todo nechat? je to tu automaticky
        setContent {
            MaterialFingerprintAppTheme {
                val items = listOf( //todo move these variables to somewhere else? Screens.kt or something in package called navigation
                    BottomNavigationItem(
                        title = "Camera",
                        selectedIcon = painterResource(id = R.drawable.photo_camera_filled),
                        unselectedIcon = painterResource(id = R.drawable.photo_camera)
                    ),
                    BottomNavigationItem(
                        title = "Analytics",
                        selectedIcon = painterResource(id = R.drawable.analytics_filled), //todo later change for analytics
                        unselectedIcon = painterResource(id = R.drawable.analytics)
                    ),
                    BottomNavigationItem(
                        title = "Settings",
                        selectedIcon = painterResource(id = R.drawable.settings_filled),
                        unselectedIcon = painterResource(id = R.drawable.settings)
                    ),
                )
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar { //todo this whole bar can be moved to some navigation package
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            //navController.navigate(item.title)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {
                                            Icon(
                                                painter = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title
                                            )

                                        }
                                    )
                                }
                            }
                        }
                    )
                    {} //todo scaffold content?
                }

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