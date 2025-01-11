package cz.cas.utia.materialfingerprintapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.opencv.android.OpenCVLoader;
import android.util.Log;

//todo is this class used? udelat si poradek v tech camera views atd.
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

        //cameraView.setContext(this); TODO pokud toto je potreba tam vratit metodu setContext() do cameraView

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