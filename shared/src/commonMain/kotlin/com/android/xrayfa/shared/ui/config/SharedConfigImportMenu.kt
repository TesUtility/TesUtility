package com.android.xrayfa.shared.ui.config

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.xrayfa.shared.resources.Res
import com.android.xrayfa.shared.resources.clipboard_import
import com.android.xrayfa.shared.resources.config_create_node
import com.android.xrayfa.shared.resources.copu
import com.android.xrayfa.shared.resources.ic_scan
import com.android.xrayfa.shared.resources.menu_bar
import com.android.xrayfa.shared.resources.menu_subscription
import com.android.xrayfa.shared.resources.scan_qr_title
import com.android.xrayfa.shared.resources.subscription_icon
import com.android.xrayfa.shared.ui.theme.XrayBrand
import com.android.xrayfa.shared.ui.theme.XrayDimens
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * INCY-styled menu with custom icon containers, branded INCY colors, and staggered menu bars.
 */
@Composable
fun IncyMenu(
    onImportFromClipboard: () -> Unit,
    onManageSubscriptions: (() -> Unit)? = null,
    onScanQr: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    importFromClipboardLabel: String = "Import from clipboard",
    manageSubscriptionsLabel: String = "Manage subscriptions",
    scanQrLabel: String = "Scan QR code",
    triggerIcon: Painter = painterResource(Res.drawable.menu_bar),
    additionalMenuItems: (@Composable (dismiss: () -> Unit) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val dismiss = { expanded = false }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(XrayDimens.IconButton),
        ) {
            Icon(
                painter = triggerIcon,
                contentDescription = importFromClipboardLabel,
                tint = Color.Unspecified,
                modifier = Modifier.size(XrayDimens.IconMd),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = dismiss,
            shape = RoundedCornerShape(24.dp),
            containerColor =
                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.92f),
            border =
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        colors =
                            listOf(
                                XrayBrand.BlueLight.copy(alpha = 0.45f),
                                XrayBrand.Blue.copy(alpha = 0.15f),
                            ),
                    ),
                ),
            shadowElevation = 16.dp,
            tonalElevation = 0.dp,
        ) {
            IncyMenuItem(
                text = importFromClipboardLabel,
                icon = painterResource(Res.drawable.copu),
                iconBgColor = XrayBrand.BlueLight.copy(alpha = 0.18f),
                iconTintColor = XrayBrand.BlueLight,
                onClick = {
                    dismiss()
                    onImportFromClipboard()
                },
            )

            if (onScanQr != null) {
                IncyMenuItem(
                    text = scanQrLabel,
                    icon = painterResource(Res.drawable.ic_scan),
                    iconBgColor = XrayBrand.CatEyeGreen.copy(alpha = 0.18f),
                    iconTintColor = XrayBrand.CatEyeGreen,
                    onClick = {
                        dismiss()
                        onScanQr()
                    },
                )
            }

            if (onManageSubscriptions != null) {
                IncyMenuItem(
                    text = manageSubscriptionsLabel,
                    icon = painterResource(Res.drawable.subscription_icon),
                    iconBgColor = XrayBrand.Blue.copy(alpha = 0.25f),
                    iconTintColor = XrayBrand.BlueLight,
                    onClick = {
                        dismiss()
                        onManageSubscriptions()
                    },
                )
            }

            if (additionalMenuItems != null) {
                HorizontalDivider(
                    color = XrayBrand.BlueLight.copy(alpha = 0.15f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                additionalMenuItems(dismiss)
            }
        }
    }
}

@Composable
fun IncyMenuItem(
    text: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconBgColor: Color = XrayBrand.Blue.copy(alpha = 0.2f),
    iconTintColor: Color = XrayBrand.BlueLight,
    textColor: Color = Color.Unspecified,
) {
    val resolvedTextColor =
        if (textColor == Color.Unspecified) MaterialTheme.colorScheme.onSurface else textColor
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = resolvedTextColor,
            )
        },
        leadingIcon = {
            Surface(
                modifier = Modifier.size(XrayDimens.IconBadge),
                shape = CircleShape,
                color = iconBgColor,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = iconTintColor,
                        modifier = Modifier.size(XrayDimens.IconSm),
                    )
                }
            }
        },
        onClick = onClick,
        colors = MenuDefaults.itemColors(
            textColor = resolvedTextColor,
        ),
        contentPadding = PaddingValues(horizontal = XrayDimens.ScreenHorizontal, vertical = XrayDimens.SpaceSm),
        modifier = modifier,
    )
}

/**
 * Вариант [IncyMenuItem] для векторных иконок Material — тот же вид (цветной кружок),
 * что и у painter-пунктов, чтобы все меню выглядели единообразно.
 */
@Composable
fun IncyMenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconBgColor: Color = XrayBrand.Blue.copy(alpha = 0.2f),
    iconTintColor: Color = XrayBrand.BlueLight,
    textColor: Color = Color.Unspecified,
) {
    val resolvedTextColor =
        if (textColor == Color.Unspecified) MaterialTheme.colorScheme.onSurface else textColor
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = resolvedTextColor,
            )
        },
        leadingIcon = {
            Surface(
                modifier = Modifier.size(XrayDimens.IconBadge),
                shape = CircleShape,
                color = iconBgColor,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTintColor,
                        modifier = Modifier.size(XrayDimens.IconSm),
                    )
                }
            }
        },
        onClick = onClick,
        colors = MenuDefaults.itemColors(textColor = resolvedTextColor),
        contentPadding = PaddingValues(horizontal = XrayDimens.ScreenHorizontal, vertical = XrayDimens.SpaceSm),
        modifier = modifier,
    )
}

/** Legacy alias pointing to IncyMenu to keep full backwards compatibility. */
@Composable
fun SharedConfigImportMenu(
    onImportFromClipboard: () -> Unit,
    onManageSubscriptions: (() -> Unit)? = null,
    onScanQr: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    importFromClipboardLabel: String = "Import from clipboard",
    manageSubscriptionsLabel: String = "Manage subscriptions",
    scanQrLabel: String = "Scan QR code",
    additionalMenuItems: (@Composable (dismiss: () -> Unit) -> Unit)? = null,
) {
    IncyMenu(
        onImportFromClipboard = onImportFromClipboard,
        onManageSubscriptions = onManageSubscriptions,
        onScanQr = onScanQr,
        modifier = modifier,
        importFromClipboardLabel = importFromClipboardLabel,
        manageSubscriptionsLabel = manageSubscriptionsLabel,
        scanQrLabel = scanQrLabel,
        additionalMenuItems = additionalMenuItems,
    )
}

/**
 * Pop-up menu anchored to "Add" buttons throughout the app.
 * Offers options to create node manually, import from clipboard, scan QR code, or manage subscriptions.
 */
@Composable
fun IncyAddPopupMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onAddManual: () -> Unit,
    onImportFromClipboard: () -> Unit,
    onScanQr: () -> Unit,
    modifier: Modifier = Modifier,
    onManageSubscriptions: (() -> Unit)? = null,
    addManualLabel: String = stringResource(Res.string.config_create_node),
    importFromClipboardLabel: String = stringResource(Res.string.clipboard_import),
    scanQrLabel: String = stringResource(Res.string.scan_qr_title),
    manageSubscriptionsLabel: String = stringResource(Res.string.menu_subscription),
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(24.dp),
        containerColor =
            MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.92f),
        border =
            BorderStroke(
                1.dp,
                Brush.linearGradient(
                    colors =
                        listOf(
                            XrayBrand.BlueLight.copy(alpha = 0.45f),
                            XrayBrand.Blue.copy(alpha = 0.15f),
                        ),
                ),
            ),
        shadowElevation = 16.dp,
        tonalElevation = 0.dp,
        modifier = modifier,
    ) {
        IncyMenuItem(
            text = addManualLabel,
            icon = painterResource(Res.drawable.menu_bar),
            iconBgColor = XrayBrand.BlueLight.copy(alpha = 0.18f),
            iconTintColor = XrayBrand.BlueLight,
            onClick = {
                onDismissRequest()
                onAddManual()
            },
        )
        IncyMenuItem(
            text = importFromClipboardLabel,
            icon = painterResource(Res.drawable.copu),
            iconBgColor = XrayBrand.BlueLight.copy(alpha = 0.18f),
            iconTintColor = XrayBrand.BlueLight,
            onClick = {
                onDismissRequest()
                onImportFromClipboard()
            },
        )
        IncyMenuItem(
            text = scanQrLabel,
            icon = painterResource(Res.drawable.ic_scan),
            iconBgColor = XrayBrand.CatEyeGreen.copy(alpha = 0.18f),
            iconTintColor = XrayBrand.CatEyeGreen,
            onClick = {
                onDismissRequest()
                onScanQr()
            },
        )
        if (onManageSubscriptions != null) {
            IncyMenuItem(
                text = manageSubscriptionsLabel,
                icon = painterResource(Res.drawable.subscription_icon),
                iconBgColor = XrayBrand.Blue.copy(alpha = 0.25f),
                iconTintColor = XrayBrand.BlueLight,
                onClick = {
                    onDismissRequest()
                    onManageSubscriptions()
                },
            )
        }
    }
}
