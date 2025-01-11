package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.lang.reflect.Constructor
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    //todo add service for calling server API for uploading images
): ViewModel()
{

    //todo nechat to takhle jako private budu na to nejak reagovat jako to delam u private atributu v browseMaterialsViewmodelu?
    private val _capturedImage = MutableStateFlow<Bitmap?>(null) //todo toto jsem chtel obalit do State ale byl by to jen empty/available takze budu prote rovnou hceckovat jestli image je nebo neni null

    private val _state = MutableStateFlow(CameraScreenState())
    val state = combine(_state, _capturedImage)
    { state, capturedImage ->
        state.copy(
            capturedImage = capturedImage
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CameraScreenState())


    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.CaptureImage -> captureImage(event)
            CameraEvent.RetakeImage -> retakeImage()
        }
    }

    private fun captureImage(event: CameraEvent.CaptureImage) {
        Log.i("CAMERATAGOS", "image captured in logs")
        _capturedImage.value = event.imageBitmap
    }

    private fun retakeImage() {
        _capturedImage.value = null
    }
}