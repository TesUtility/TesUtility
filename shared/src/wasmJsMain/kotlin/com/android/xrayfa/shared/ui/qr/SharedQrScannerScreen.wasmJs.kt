package com.android.xrayfa.shared.ui.qr

import androidx.compose.runtime.Composable
import com.android.xrayfa.shared.ui.rememberSettingsUiLabels
import com.android.xrayfa.shared.ui.settings.SharedInDevelopmentScreen

@Composable
actual fun SharedQrScannerScreen(
    onResult: (String) -> Unit,
    onBack: () -> Unit,
    title: String,
    permissionRequiredMessage: String,
) {
    val labels = rememberSettingsUiLabels()
    SharedInDevelopmentScreen(
        title = title,
        message = permissionRequiredMessage,
        onBack = onBack,
        backContentDescription = labels.cancelLabel,
    )
}
