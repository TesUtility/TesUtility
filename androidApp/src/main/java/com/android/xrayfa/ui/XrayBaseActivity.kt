package com.android.xrayfa.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalConfiguration
import com.android.xrayfa.ui.theme.V2rayForAndroidUITheme

abstract class XrayBaseActivity: ComponentActivity(){

    @Composable
    abstract fun Content(isLandscape: Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            DisposableEffect(Unit) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
                    navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
                )
                onDispose {}
            }

            V2rayForAndroidUITheme(
                darkTheme = true
            ) {
                Content(false)
            }
        }
    }

}

@Deprecated("use scene instead")
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ShowContentWithOrientation(
    content: @Composable (isLandscape: Boolean) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    content(isLandscape)
}