package org.una.danger.android.android_camera_example_2022.screens

import android.view.ViewGroup
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import org.una.danger.android.android_camera_example_2022.extensions.safeImage
import org.una.danger.android.android_camera_example_2022.hooks.useFaceAnalyzer
import org.una.danger.android.android_camera_example_2022.usecases.useCamera


@Composable
fun CameraPreviewScreen() {
    val faceAnalyzer = useFaceAnalyzer()
    CameraPreview { imageProxy ->
        imageProxy.safeImage { image ->
            faceAnalyzer(image, imageProxy.imageInfo) {
                image.close()
                imageProxy.close()
            }
        }
    }
}

@Composable
fun CameraPreview(analyzer: ImageAnalysis.Analyzer) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val camera = useCamera()
    BoxWithConstraints {
        AndroidView(
            factory = { ctx ->
                val scaleType = PreviewView.ScaleType.FILL_CENTER
                val previewView = PreviewView(ctx).apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
                val executor = ContextCompat.getMainExecutor(ctx)
                cameraProviderFuture.addListener(
                    {
                        camera(
                            maxWidth,
                            maxHeight,
                            cameraProviderFuture,
                            previewView,
                            executor,
                            analyzer,
                            lifecycleOwner
                        )
                    },
                    executor
                )
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
