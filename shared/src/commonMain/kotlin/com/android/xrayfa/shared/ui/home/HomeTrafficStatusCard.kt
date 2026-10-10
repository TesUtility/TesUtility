package com.android.xrayfa.shared.ui.home
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.xrayfa.shared.ui.theme.XrayBrand
import kotlin.math.roundToInt

@Composable
fun HomeTrafficStatusCard(
    isConnected: Boolean,
    uploadSpeedKbps: Double,
    downloadSpeedKbps: Double,
    uploadLabel: String,
    downloadLabel: String,
    modifier: Modifier = Modifier,
    framed: Boolean = true,
) {
    val body: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = if (framed) 20.dp else 4.dp,
                    vertical = if (framed) 18.dp else 4.dp,
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TrafficBar(
                icon = Icons.Default.KeyboardArrowUp,
                label = uploadLabel,
                speedKbps = if (isConnected) uploadSpeedKbps else 0.0,
                accentStart = XrayBrand.BlueLight,
                accentEnd = XrayBrand.Blue,
                formatSpeed = ::formatSpeedKbps, // ← используем ваш существующий
            )
            TrafficBar(
                icon = Icons.Default.KeyboardArrowDown,
                label = downloadLabel,
                speedKbps = if (isConnected) downloadSpeedKbps else 0.0,
                accentStart = XrayBrand.Blue,
                accentEnd = XrayBrand.BlueDeep,
                formatSpeed = ::formatSpeedKbps,
            )
        }
    }

    if (framed) {
        ElevatedCard(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors =
                CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
        ) {
            body()
        }
    } else {
        Box(modifier = modifier.fillMaxWidth()) { body() }
    }
}

@Composable
private fun HomeTrafficSpeedItem(
    label: String,
    speedKbps: Double,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        }
        androidx.compose.foundation.layout.Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = label.replaceFirstChar { it.uppercaseChar() },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "${formatSpeedKbps(speedKbps)} KB/s",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

private fun formatSpeedKbps(speedKbps: Double): String {
    val rounded = (speedKbps * 10.0).roundToInt() / 10.0
    return if (rounded % 1.0 == 0.0) {
        rounded.toInt().toString()
    } else {
        rounded.toString()

    }

}
@Composable
private fun TrafficBar(
    icon: ImageVector,
    label: String,
    speedKbps: Double,
    accentStart: Color,
    accentEnd: Color,
    formatSpeed: (Double) -> String,
) {
    val maxKbps = 10.0 * 1024.0
    val targetProgress = (speedKbps / maxKbps).coerceIn(0.0, 1.0).toFloat()

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 400),
        label = "trafficProgress",
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

// ─── Строка: подпись + значение ───
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentStart,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = "${formatSpeed(speedKbps)} KB/s",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

// ─── Прогресс-бар ───
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.horizontalGradient(listOf(accentStart, accentEnd))
                    ),
            )
        }
    }
}
