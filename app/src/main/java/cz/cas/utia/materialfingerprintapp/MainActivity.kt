package cz.cas.utia.materialfingerprintapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.cas.utia.materialfingerprintapp.core.launch.presentation.LaunchScreenRoot
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.CustomAppTheme
import dagger.hilt.android.AndroidEntryPoint
import org.opencv.android.OpenCVLoader

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!OpenCVLoader.initLocal()) {
            Log.e("OpenCV", "Initialization failed")
        }

        setContent {
            CustomAppTheme {
                LaunchScreenRoot()
            }
        }
    }
}