package com.example.saa.presentation.ui.access_denied

import android.content.res.Configuration
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.saa.NavRoutes
import com.example.saa.R
import com.example.saa.ui.theme.Background
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.SaaTheme
import com.example.saa.ui.theme.TextOnButton
import com.example.saa.ui.theme.accessDeniedDescription
import com.example.saa.ui.theme.accessDeniedTitle
import com.example.saa.ui.theme.loginButton

// ── Access Denied Screen ─────────────────────────────────────────────────────
// Figma frame: k-7zJk2B7s — [iOS] Access denied
// mms_2 container (Node 6885:9522): gap=24px, padding=40px 20px, bg=#00101A,
//   border-radius=7.304px, flex-direction=column, align-items=center
// Content is TOP-ALIGNED with FIXED 24dp gaps (no weight-based spacers).
// TR-002: Uses LocalOnBackPressedDispatcherOwner instead of Activity.finish()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessDeniedScreen(navController: NavController) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val cdBackArrow = stringResource(R.string.cd_back_arrow)

    Scaffold(
        topBar = {
            // TopNavigation (Node 6885:9494): height=89px, bg=#00101A opacity=0.9
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = { backDispatcher?.onBackPressed() },
                        modifier = Modifier.semantics { contentDescription = cdBackArrow },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background.copy(alpha = 0.9f),
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // bg (Node 6885:9491): MM_MEDIA_Keyvisual BG positioned to CenterEnd + Shadow Left
            Image(
                painter = painterResource(R.drawable.mm_media_keyvisual_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alignment = Alignment.CenterEnd,
            )
            // Shadow Left: linear-gradient(90deg, #00101A 0.07%, #10181F 18.61%, transparent 77.2%)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0.0007f to Color(0xFF00101A),
                                0.1861f to Color(0xFF10181F),
                                0.772f to Color(0x0000101A),
                            ),
                        ),
                    ),
            )

            // mms_2_Open secret box (Node 6885:9522)
            // Solid #00101A background with top rounded corners (7.304px)
            // padding=40px 20px, gap=24px between children (fixed Spacers)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 7.dp, topEnd = 7.dp))
                    .background(Background)
                    .padding(horizontal = 20.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // mms_Header (Node 6885:9523): gap=8px, height=80px, flex-column
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Title (Node 6885:9525): Montserrat Bold 18sp, #FFEA9E, centered
                    Text(
                        text = stringResource(R.string.access_denied_title),
                        style = MaterialTheme.typography.accessDeniedTitle,
                        color = ButtonPrimaryBg,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Rectangle 16 (Node 6885:9526): divider, color=#2E3940
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Description (Node 6885:9528): Montserrat Medium 14sp, white, centered
                    Text(
                        text = stringResource(R.string.access_denied_description),
                        style = MaterialTheme.typography.accessDeniedDescription,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // gap=24px: mms_Header → mms_2.1
                Spacer(modifier = Modifier.height(24.dp))

                // mms_2.1 (Node 6885:9529): 320×248.433px (aspect-ratio 76/59), decorative
                Image(
                    painter = painterResource(R.drawable.mm_media_not_found),
                    contentDescription = null,
                    modifier = Modifier
                        .width(320.dp)
                        .height(249.dp),
                    contentScale = ContentScale.Fit,
                )

                // gap=24px: mms_2.1 → Rectangle 18
                Spacer(modifier = Modifier.height(24.dp))

                // Rectangle 18 (Node 6885:9530): divider, color=#2E3940
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                // gap=24px: Rectangle 18 → mms_2.2_Button
                Spacer(modifier = Modifier.height(24.dp))

                // mms_2.2_Button (Node 6885:9531): height=40px, bg=#FFEA9E, radius=4px,
                // padding=10px, align-self=stretch → fillMaxWidth
                // FR-003+FR-004: navigate to Home, clear AccessDenied from back stack
                Button(
                    onClick = {
                        navController.navigate(NavRoutes.Home.route) {
                            popUpTo(NavRoutes.AccessDenied.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimaryBg),
                    contentPadding = PaddingValues(10.dp),
                ) {
                    Text(
                        text = stringResource(R.string.access_denied_button),
                        style = MaterialTheme.typography.loginButton,
                        color = TextOnButton,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccessDeniedScreenPreviewLight() {
    SaaTheme { AccessDeniedScreen(navController = rememberNavController()) }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AccessDeniedScreenPreviewDark() {
    SaaTheme { AccessDeniedScreen(navController = rememberNavController()) }
}
