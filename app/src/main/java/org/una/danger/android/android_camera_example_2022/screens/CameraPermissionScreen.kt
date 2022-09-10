package org.una.danger.android.android_camera_example_2022.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.una.danger.android.android_camera_example_2022.hooks.UsePermission

@Composable
fun CameraPermissionScreen() {
    var isGranted by remember { mutableStateOf<Boolean?>(null) }
    UsePermission { granted ->
        isGranted = granted
    }
    val granted = isGranted ?: run {
        Text(text = "パーミッションを確認中...")
        return
    }
    if (granted) {
        Text(text = "権限OK")
    } else {
        Text(text = "権限NG")
    }
}