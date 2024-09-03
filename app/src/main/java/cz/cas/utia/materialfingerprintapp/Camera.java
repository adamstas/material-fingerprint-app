package cz.cas.utia.materialfingerprintapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.opencv.android.OpenCVLoader;
import android.util.Log;

public class Camera extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CustomCameraView cameraView;

    // Static block to load OpenCV library
    static {
        if (!OpenCVLoader.initLocal()) {
            // Handle initialization error
            Log.e(TAG, "OpenCV initialization failed");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Initialize the custom camera view
        cameraView = findViewById(R.id.camera_view);

        cameraView.setContext(this);

        // Start the camera view
        cameraView.setCameraPermissionGranted();
        cameraView.enableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cameraView != null) {
            cameraView.enableView();
        }
    }

}