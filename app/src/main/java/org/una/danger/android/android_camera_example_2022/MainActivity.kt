package org.una.danger.android.android_camera_example_2022

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.una.danger.android.android_camera_example_2022.hooks.UsePermission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                CameraPermissionScreen()
            }
        }
    }
}

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