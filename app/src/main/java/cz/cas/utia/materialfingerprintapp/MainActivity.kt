package cz.cas.utia.materialfingerprintapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import cz.cas.utia.materialfingerprintapp.core.navigation.MainNavigation
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.CustomAppTheme
import cz.cas.utia.materialfingerprintapp.core.ui.theme.original.MaterialFingerprintAppTheme
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialDatabase
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.initialData
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.BrowseMaterialsScreen
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.BrowseMaterialsViewModel
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.PermissionsScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    //todo dependency injection
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            MaterialDatabase::class.java,
            "materials.db"
        ).build()
    }
    private val viewModel by viewModels<BrowseMaterialsViewModel>(
        factoryProducer = {
            object: ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T: ViewModel> create(modelClass: Class<T>): T {
                    return BrowseMaterialsViewModel(db.materialDao) as T
                }
            }
        }
    )

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //todo nechat? je to tu automaticky - asi nechat, je to kvuli roztahovani UI spravne

        //val intent = Intent(this, Camera::class.java)
        //startActivity(intent)
        setContent {
            CustomAppTheme {
               // MainNavigation()

                lifecycleScope.launch { //todo init somewhere else
                    if (db.materialDao.getMaterialsCount() == 0) {
                        //db.materialDao.deleteAllMaterials()
                        db.materialDao.insertMaterials(initialData())
                    }
                }

                val state by viewModel.state.collectAsState()
                BrowseMaterialsScreen(state = state, onEvent = viewModel::onEvent)
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