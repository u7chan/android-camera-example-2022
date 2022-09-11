package org.una.danger.android.android_camera_example_2022.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.face.Face
import org.una.danger.android.android_camera_example_2022.extensions.safeImage
import org.una.danger.android.android_camera_example_2022.hooks.useFaceAnalyzer
import org.una.danger.android.android_camera_example_2022.lib.BitmapUtils
import org.una.danger.android.android_camera_example_2022.usecases.useCamera

data class FaceDetectResult(
    val bitmap: Bitmap?,
    val faces: List<Face>
)

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreviewScreen() {
    var faceDetectResult: FaceDetectResult? by remember { mutableStateOf(null) }
    val faceAnalyzer = useFaceAnalyzer()
    Box {
        CameraPreview { imageProxy ->
            imageProxy.safeImage { image ->
                faceAnalyzer(image, imageProxy.imageInfo) { faces ->
                    faceDetectResult = FaceDetectResult(
                        if (faces.isNotEmpty()) BitmapUtils.getBitmap(imageProxy) else null,
                        faces
                    )
                    image.close()
                    imageProxy.close()
                }
            }
        }
        // Overlay
        faceDetectResult?.also {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .size(100.dp, 140.dp)
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                it.bitmap?.also { safeBitmap ->
                    Image(
                        bitmap = safeBitmap.asImageBitmap(),
                        contentDescription = "",
                    )
                }
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
