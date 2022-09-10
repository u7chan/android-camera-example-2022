package org.una.danger.android.android_camera_example_2022.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.una.danger.android.android_camera_example_2022.hooks.usePermission

private sealed class PermissionState {
    object Nothing : PermissionState()
    object Denied : PermissionState()
    object Granted : PermissionState()
}

@Composable
fun CameraPermissionScreen(content: @Composable () -> Unit) {
    var permissionState by remember { mutableStateOf<PermissionState>(PermissionState.Nothing) }
    usePermission { granted ->
        permissionState = if (granted) PermissionState.Granted else PermissionState.Denied
    }
    when (permissionState) {
        PermissionState.Nothing -> Text(text = "パーミッションを確認中...")
        PermissionState.Denied -> Text(text = "権限NG")
        PermissionState.Granted -> content()
    }
}