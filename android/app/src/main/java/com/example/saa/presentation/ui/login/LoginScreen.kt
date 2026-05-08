package com.example.saa.presentation.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.saa.BuildConfig
import com.example.saa.NavRoutes
import com.example.saa.R
import com.example.saa.presentation.ui.login.components.LanguageDropdownMenu
import com.example.saa.ui.theme.Background
import com.example.saa.ui.theme.ButtonPrimaryBg
import com.example.saa.ui.theme.TextOnButton
import com.example.saa.ui.theme.TextOnDark
import com.example.saa.ui.theme.loginButton
import com.example.saa.ui.theme.loginCopyright
import com.example.saa.ui.theme.loginDescription
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Credential Manager — khởi tạo một lần, không suspend
    val credentialManager = remember { CredentialManager.create(context) }
    val googleIdOption = remember {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()
    }
    val credentialRequest = remember {
        GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    // Side-effects
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            navController.navigate(NavRoutes.Home.route) {
                popUpTo(NavRoutes.Login.route) { inclusive = true }
            }
            viewModel.consumeNavigationEvent()
        }
    }

    LaunchedEffect(uiState.isAccessDenied) {
        if (uiState.isAccessDenied) {
            navController.navigate(NavRoutes.AccessDenied.route) {
                popUpTo(NavRoutes.Login.route) { inclusive = true }
            }
            viewModel.consumeNavigationEvent()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background),
        ) {
            // ── Layer 1: Full-screen key visual background (MM_MEDIA_Keyvisual BG) ──
            Image(
                painter = painterResource(R.drawable.mm_media_keyvisual_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            // ── Layer 2: Header gradient overlay (375×104dp, opacity 0.9) ────────
            // linear-gradient(180deg, #00101A 0%, rgba(0,16,26,0.30) 76.44%, → transparent 100%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0000f to Background,
                                0.7644f to Background.copy(alpha = 0.30f),
                                0.8462f to Background.copy(alpha = 0.20f),
                                0.8870f to Background.copy(alpha = 0.15f),
                                0.9279f to Background.copy(alpha = 0.10f),
                                0.9639f to Background.copy(alpha = 0.05f),
                                1.0000f to Background.copy(alpha = 0.00f),
                            )
                        )
                    )
                    .alpha(0.9f),
            )

            // ── Layer 3: Main content ─────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            ) {
                // Status bar inset + 8dp gap → logo at y≈52 (Figma: iOS status bar 44pt + 8pt gap)
                Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding() + 8.dp))

                // ── Header row: Logo (48×44dp) + Language Selector (90×32dp) ─────
                val flagMap = mapOf("VN" to "🇻🇳", "EN" to "🇬🇧")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // mms_2_mm_media_logo — 48×44dp
                    Image(
                        painter = painterResource(R.drawable.mm_media_logo_homepage),
                        contentDescription = "SAA 2025",
                        modifier = Modifier.size(width = 48.dp, height = 44.dp),
                        contentScale = ContentScale.Fit,
                    )

                    // mms_2.1_language — 90×32dp, padding(4,0,4,8), gap=8dp, radius=4dp
                    Box {
                        val triggerDescription = context.getString(
                            R.string.language_selector_description,
                            uiState.selectedLanguage,
                        )
                        Row(
                            modifier = Modifier
                                .size(width = 90.dp, height = 32.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { viewModel.showLanguageSelector() }
                                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                                .semantics { contentDescription = triggerDescription },
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = flagMap[uiState.selectedLanguage] ?: "🌐")
                            Text(
                                text = uiState.selectedLanguage,
                                style = MaterialTheme.typography.loginButton,
                                color = TextOnDark,
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = TextOnDark,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        LanguageDropdownMenu(
                            expanded = uiState.showLanguageSelector,
                            selectedLanguage = uiState.selectedLanguage,
                            onLanguageSelected = { viewModel.setLanguage(it) },
                            onDismiss = { viewModel.dismissLanguageSelector() },
                        )
                    }
                }

                // Gap: header bottom (y≈96) → ROOT FURTHER (y=252) = 156dp
                Spacer(modifier = Modifier.height(156.dp))

                // ── mms_3_MM_MEDIA_Logo/RootFuther — 247×109dp, left-aligned ─────
                Image(
                    painter = painterResource(R.drawable.mm_media_logo_rootfuther),
                    contentDescription = "ROOT FURTHER",
                    modifier = Modifier.size(width = 247.dp, height = 109.dp),
                    contentScale = ContentScale.Fit,
                )

                // Gap: ROOT FURTHER bottom (y=361) → description (y=393) = 32dp
                Spacer(modifier = Modifier.height(32.dp))

                // ── mms_4_content — 335×40dp, Montserrat Light 14sp, left ─────────
                Text(
                    text = stringResource(R.string.login_description),
                    modifier = Modifier.width(335.dp),
                    style = MaterialTheme.typography.loginDescription,
                    color = TextOnDark,
                    textAlign = TextAlign.Start,
                )

                // Gap: description bottom (y=433) → button (y=626) = 193dp (proportional)
                Spacer(modifier = Modifier.weight(193f))

                // ── mms_5_Button — 246×40dp, centered, #FFEA9E, radius=4dp ────────
                Button(
                    onClick = {
                        scope.launch {
                            runCatching {
                                val result = credentialManager.getCredential(context, credentialRequest)
                                val googleCred = result.credential as? GoogleIdTokenCredential
                                    ?: error("Unexpected credential type")
                                viewModel.loginWithGoogle(googleCred.idToken)
                            }.onFailure { e -> viewModel.handleCredentialError(e) }
                        }
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .size(width = 246.dp, height = 40.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonPrimaryBg,
                        disabledContainerColor = ButtonPrimaryBg.copy(alpha = 0.7f),
                    ),
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = TextOnButton,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        // Label: "LOGIN With Google" + Google icon (gap=8dp)
                        Text(
                            text = stringResource(R.string.login_button_label),
                            style = MaterialTheme.typography.loginButton,
                            color = TextOnButton,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(R.drawable.mm_media_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                // Gap: button bottom (y=666) → footer start (y=764) = 98dp (proportional)
                Spacer(modifier = Modifier.weight(98f))

                // ── mms_6_Copyright — 196×16dp, Montserrat Regular 12sp, centered ─
                Text(
                    text = stringResource(R.string.copyright_text),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.loginCopyright,
                    color = TextOnDark,
                    textAlign = TextAlign.Center,
                )

                // 16dp gap to screen bottom + navigation bar inset
                Spacer(modifier = Modifier.height(16.dp + innerPadding.calculateBottomPadding()))
            }

        }
    }
}
