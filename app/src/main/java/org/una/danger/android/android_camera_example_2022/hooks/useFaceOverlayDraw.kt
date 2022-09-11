package org.una.danger.android.android_camera_example_2022.hooks

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.face.Face

@Composable
fun useFaceOverlayDraw(): (Bitmap, List<Face>) -> Bitmap {
    val context = LocalContext.current
    val paint = Paint().apply {
        color = Color.YELLOW
        strokeWidth = context.resources.displayMetrics.density * 4 // dp to px
        style = Paint.Style.STROKE
    }
    return { bitmap, faces ->
        faces.forEach { face ->
            val bounds = face.boundingBox
            val canvas = Canvas(bitmap)
            canvas.drawRect(bounds, paint)
        }
        bitmap
    }
}