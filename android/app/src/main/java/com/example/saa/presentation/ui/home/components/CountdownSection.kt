package com.example.saa.presentation.ui.home.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.saa.R
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.homeComingSoon
import com.example.saa.ui.theme.homeCountdownDigit
import com.example.saa.ui.theme.homeCountdownLabel

private val digitBoxGradient = Brush.verticalGradient(
    colors = listOf(Color.White, Color(0x1AFFFFFF))
)

@Composable
fun CountdownSection(
    days: Long,
    hours: Long,
    minutes: Long,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(R.string.home_coming_soon),
            style = MaterialTheme.typography.homeComingSoon,
            color = TextOnDark,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
        ) {
            CountdownUnit(
                value = days,
                label = stringResource(R.string.home_countdown_days),
                width = 72,
            )
            CountdownUnit(
                value = hours,
                label = stringResource(R.string.home_countdown_hours),
                width = 72,
            )
            CountdownUnit(
                value = minutes,
                label = stringResource(R.string.home_countdown_minutes),
                width = 92,
            )
        }
    }
}

@Composable
private fun CountdownUnit(
    value: Long,
    label: String,
    width: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.width(width.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.height(56.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val tens = (value / 10).coerceAtMost(9)
            val units = value % 10
            DigitBox(digit = tens.toString())
            DigitBox(digit = units.toString())
        }
        Text(
            text = label,
            style = MaterialTheme.typography.homeCountdownLabel,
            color = TextOnDark,
        )
    }
}

@Composable
private fun DigitBox(
    digit: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(width = 32.dp, height = 56.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Blurred frosted-glass background layer
        val bgModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Modifier.graphicsLayer {
                renderEffect = android.graphics.RenderEffect
                    .createBlurEffect(17f, 17f, android.graphics.Shader.TileMode.CLAMP)
                    .asComposeRenderEffect()
            }
        } else Modifier

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp))
                .then(bgModifier)
                .background(digitBoxGradient, alpha = 0.5f)
                .border(
                    width = 0.5.dp,
                    color = ButtonPrimaryBg.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                ),
        )

        // Digit text — rendered on top, unaffected by the blur
        Text(
            text = digit,
            style = MaterialTheme.typography.homeCountdownDigit,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}
