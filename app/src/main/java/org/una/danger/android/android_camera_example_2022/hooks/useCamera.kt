package org.una.danger.android.android_camera_example_2022.usecases

import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor
import kotlin.math.abs

fun useCamera(): (
    maxWidth: Dp,
    maxHeight: Dp,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    previewView: PreviewView,
    executor: Executor,
    analyzer: ImageAnalysis.Analyzer,
    lifecycleOwner: LifecycleOwner,
) -> Unit {
    return { maxWidth, maxHeight, cameraProviderFuture, previewView, executor, analyzer, lifecycleOwner ->
        val screenAspectRatio = aspectRatio(width = maxWidth, height = maxHeight)
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .build()
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(executor, analyzer)
            }
        val cameraSelector = CameraSelector.Builder()
            //.requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
}

private fun aspectRatio(width: Dp, height: Dp): Int {
    val previewRatio = max(width, height).value.toDouble() / min(width, height).value.toDouble()
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
}

private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0