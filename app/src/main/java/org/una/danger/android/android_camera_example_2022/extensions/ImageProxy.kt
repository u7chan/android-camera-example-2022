package org.una.danger.android.android_camera_example_2022.extensions

import android.annotation.SuppressLint
import android.media.Image
import androidx.camera.core.ImageProxy

@SuppressLint("UnsafeOptInUsageError")
fun ImageProxy.safeImage(callback: (image: Image) -> Unit) {
    this.image?.also { image ->
        callback(image)
    }
}