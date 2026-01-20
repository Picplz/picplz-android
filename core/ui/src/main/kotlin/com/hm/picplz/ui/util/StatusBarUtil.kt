package com.hm.picplz.ui.util

import android.app.Activity
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Suppress("DEPRECATION")
@Composable
fun SetStatusBarStyle(
    statusBarColor: Color = Color.Transparent,
    isLightStatusBar: Boolean = true,
) {
    val view = LocalView.current
    val context = LocalContext.current

    LaunchedEffect(statusBarColor, isLightStatusBar) {
        val activity = context as? Activity ?: return@LaunchedEffect
        activity.window?.apply {
            this.statusBarColor = statusBarColor.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = isLightStatusBar
        }
    }
}

@Suppress("DEPRECATION")
fun Activity.setStatusBarStyle(
    view: View,
    statusBarColor: Color = Color.Transparent,
    isLightStatusBar: Boolean = true,
) {
    window?.apply {
        this.statusBarColor = statusBarColor.toArgb()
        WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = isLightStatusBar
    }
}
