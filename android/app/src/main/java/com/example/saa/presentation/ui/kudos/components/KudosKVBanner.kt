package com.example.saa.presentation.ui.kudos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.saa.R
import com.example.saa.ui.theme.KudosGold

// Figma node: mms_A_KV Kudos (6885:9066) — text content over the full-screen KV BG
// The colorful keyvisual bg is rendered in KudosScreen (full 723dp, same as HomeScreen).
// This banner only renders the dark gradient overlay + text content (subtitle + logo row).
// Height: 240dp — gives enough room for the top bar + subtitle + logo
// Content: bottom-start aligned, 20dp horizontal padding
// Gradient: #00101A fully opaque at very top → transparent at bottom
private val KVGradient = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to Color(0xE600101A),  // ~90% opaque (hides keyvisual behind top bar area)
        0.4f to Color(0x8000101A),  // 50%
        0.65f to Color(0x4000101A), // 25%
        0.85f to Color(0x1A00101A), // ~10%
        1.0f to Color(0x0000101A),  // transparent at bottom
    ),
)

@Composable
fun KudosKVBanner(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(KVGradient),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            // Subtitle
            Text(
                text = "Hệ thống ghi nhận và cảm ơn",
                color = KudosGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Logo row: Sun* mark + KUDOS text
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.img_saa_logo),
                    contentDescription = "Sun* Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(width = 49.dp, height = 38.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "KUDOS",
                    color = KudosGold,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF00101A)
@Composable
private fun KudosKVBannerPreview() {
    KudosKVBanner()
}
