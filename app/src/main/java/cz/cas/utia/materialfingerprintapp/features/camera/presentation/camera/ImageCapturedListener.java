package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera;

import android.graphics.Bitmap;

//todo nechat interface takhle mimo?
public interface ImageCapturedListener {
    void onImageCaptured(Bitmap bitmap);
}
