package org.una.danger.android.android_camera_example_2022.hooks

import android.media.Image
import androidx.camera.core.ImageInfo
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

fun useFaceAnalyzer(): (image: Image, imageInfo: ImageInfo, onComplete: () -> Unit) -> Unit {
    val detector = FaceDetection.getClient(
        createFaceDetectorOptions(FaceDetectorOption.HighAccuracy)
    )
    return { image, imageInfo, onComplete ->
        val inputImage = InputImage.fromMediaImage(image, imageInfo.rotationDegrees)
        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                println("### Detector: ${faces.size}")
                for (face in faces) {
                    val bounds = face.boundingBox
                    println("### Face: $bounds")
                }
            }
            .addOnFailureListener {
                // TODO
            }
            .addOnCompleteListener {
                onComplete()
            }
    }
}

private enum class FaceDetectorOption {
    HighAccuracy,
    RealTime
}

private fun createFaceDetectorOptions(option: FaceDetectorOption): FaceDetectorOptions {
    return when (option) {
        // High-accuracy landmark detection and face classification
        FaceDetectorOption.HighAccuracy -> FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()
        // Real-time contour detection
        FaceDetectorOption.RealTime -> FaceDetectorOptions.Builder()
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()
    }
}