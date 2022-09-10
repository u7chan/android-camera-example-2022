package org.una.danger.android.android_camera_example_2022.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.una.danger.android.android_camera_example_2022.hooks.UsePermission

@Composable
fun CameraPermissionScreen(content: @Composable () -> Unit) {
    var isGranted by remember { mutableStateOf<Boolean?>(null) }
    UsePermission { granted ->
        isGranted = granted
    }
    val permissionGranted = isGranted ?: run {
        Text(text = "パーミッションを確認中...")
        return
    }
    if (permissionGranted) {
        content()
    } else {
        Text(text = "権限NG")
    }
}