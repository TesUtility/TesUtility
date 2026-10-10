package com.android.xrayfa.shared.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.xrayfa.shared.resources.Res
import com.android.xrayfa.shared.resources.app_name
import com.android.xrayfa.shared.ui.config.IncyMenu
import com.android.xrayfa.shared.ui.theme.XrayBrand
import com.android.xrayfa.shared.ui.theme.XrayDimens
import com.android.xrayfa.shared.ui.theme.XrayIcons
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onSettingsClick: () -> Unit,
    onImportFromClipboard: (() -> Unit)? = null,
    onManageSubscriptions: (() -> Unit)? = null,
    onScanQr: (() -> Unit)? = null,
    onConfigClick: (() -> Unit)? = null,
) {
    val versionName = "1.7.1"
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(Res.string.app_name),
                    fontWeight = FontWeight.Bold,
                    color = XrayBrand.BlueLight,
                )
                Text(
                    text = "v$versionName",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.4.sp,
                    style =
                        LocalTextStyle.current.copy(
                            brush =
                                Brush.linearGradient(
                                    colors =
                                        listOf(
                                            XrayBrand.BlueLight,
                                            XrayBrand.Blue,
                                            XrayBrand.BlueDeep,
                                        ),
                                ),
                        ),
                )
            }
        },
        actions = {
            if (onImportFromClipboard != null) {
                IncyMenu(
                    onImportFromClipboard = onImportFromClipboard,
                    onManageSubscriptions = onManageSubscriptions,
                    onScanQr = onScanQr,
                )
            }
            onConfigClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = XrayIcons.Config,
                        contentDescription = "Config",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(XrayDimens.IconMd),
                    )
                }
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = XrayIcons.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(XrayDimens.IconMd),
                )
            }
        },
    )
}
