package kite1412.gatetik.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kite1412.gatetik.designsystem.theme.Emerald500
import kotlin.random.Random

@Composable
fun AudioLevelMeter(
    level: Float,
    modifier: Modifier = Modifier,
    barCount: Int = 5,
    barWidth: Dp = 4.dp,
    maxBarHeight: Dp = 24.dp,
    activeColor: Color = Emerald500,
    inactiveColor: Color = Color.Gray.copy(alpha = 0.3f)
) {
    Row(
        modifier = modifier.height(maxBarHeight),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(barCount) { index ->
            val variation = if (level > 0) Random.nextFloat() * 0.3f else 0f
            val barLevel = (level + variation).coerceIn(0f, 1f)
            
            val animatedHeight by animateFloatAsState(
                targetValue = barLevel * maxBarHeight.value,
                label = "BarHeight"
            )

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(animatedHeight.dp.coerceAtLeast(2.dp))
                    .clip(RoundedCornerShape(barWidth / 2))
                    .background(if (level > 0) activeColor else inactiveColor)
            )
        }
    }
}
