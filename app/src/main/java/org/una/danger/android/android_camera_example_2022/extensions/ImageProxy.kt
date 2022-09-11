package org.una.danger.android.android_camera_example_2022.extensions

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.media.Image
import androidx.camera.core.ImageProxy
import org.una.danger.android.android_camera_example_2022.libs.BitmapUtils

@SuppressLint("UnsafeOptInUsageError")
fun ImageProxy.safeImage(callback: (Image) -> Unit) {
    this.image?.also { image ->
        callback(image)
    }
}

@SuppressLint("UnsafeOptInUsageError")
fun ImageProxy.toBitmap(): Bitmap? {
    return BitmapUtils.getBitmap(this)
}