package com.example.saa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SaaDarkColorScheme = darkColorScheme(
    primary = ButtonPrimaryBg,
    onPrimary = TextOnButton,
    background = Background,
    onBackground = TextOnDark,
    surface = Background,
    onSurface = TextOnDark,
    outlineVariant = DividerLine,
)

@Composable
fun SaaTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = SaaDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
