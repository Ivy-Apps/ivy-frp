package com.ivy.frp.view.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@SuppressLint("ComposableNaming")
@Composable
fun onScreenStart(
    cleanUp: () -> Unit = {},
    start: () -> Unit
) {
    DisposableEffect(navigation().currentScreen) {
        start()
        onDispose { cleanUp() }
    }
}