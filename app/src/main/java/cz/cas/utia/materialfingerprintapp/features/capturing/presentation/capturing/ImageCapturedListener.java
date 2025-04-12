package cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing;

import android.graphics.Bitmap;

//todo nechat interface takhle mimo?
public interface ImageCapturedListener {
    void onImageCaptured(Bitmap bitmap);
}
