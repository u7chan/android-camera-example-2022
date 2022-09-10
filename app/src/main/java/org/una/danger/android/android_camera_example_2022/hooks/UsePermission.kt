package org.una.danger.android.android_camera_example_2022.hooks

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun UsePermission(onGranted: (Boolean) -> Unit) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            onGranted(granted)
        }
    val checkSelfPermission = ContextCompat.checkSelfPermission(
        LocalContext.current,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
    if (checkSelfPermission) {
        onGranted(true)
    } else {
        SideEffect {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
}