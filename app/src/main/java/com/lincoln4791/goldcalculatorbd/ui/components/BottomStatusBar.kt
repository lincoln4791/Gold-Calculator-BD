package com.lincoln4791.goldcalculatorbd.ui.components

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SetNavigationBarColor(color: Color) {
/*    val window = (LocalContext.current as? Activity)?.window
    val useLightIcons = color.luminance() > 0.5 // Decide based on the color brightness

    SideEffect {
        window?.navigationBarColor = color.toArgb()
        val windowInsetsController = WindowInsetsControllerCompat(window, window?.decorView)
        windowInsetsController.isAppearanceLightNavigationBars = useLightIcons
    }*/
}