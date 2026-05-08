package com.example.saa.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.saa.R

// ── Montserrat via Google Fonts Compose (Option A) ─────────────────────────
private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val montserratFont = GoogleFont("Montserrat")

private val MontserratLight = FontFamily(
    Font(googleFont = montserratFont, fontProvider = googleFontProvider, weight = FontWeight.Light)
)
private val MontserratMedium = FontFamily(
    Font(googleFont = montserratFont, fontProvider = googleFontProvider, weight = FontWeight.Medium)
)
private val MontserratRegular = FontFamily(
    Font(googleFont = montserratFont, fontProvider = googleFontProvider, weight = FontWeight.Normal)
)
private val MontserratBold = FontFamily(
    Font(googleFont = montserratFont, fontProvider = googleFontProvider, weight = FontWeight.Bold)
)

// ── SAA login screen typography tokens ─────────────────────────────────────
val Typography.loginDescription: TextStyle
    get() = TextStyle(
        fontFamily = MontserratLight,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
    )

val Typography.loginButton: TextStyle
    get() = TextStyle(
        fontFamily = MontserratMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )

val Typography.loginCopyright: TextStyle
    get() = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    )

// ── SAA access denied screen typography tokens ──────────────────────────────
val Typography.accessDeniedTitle: TextStyle
    get() = TextStyle(
        fontFamily = MontserratBold,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )

val Typography.accessDeniedDescription: TextStyle
    get() = TextStyle(
        fontFamily = MontserratMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )

// ── Home screen typography tokens ───────────────────────────────────────────
val Typography.homeComingSoon: TextStyle
    get() = TextStyle(
        fontFamily = MontserratLight,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    )

val Typography.homeCountdownLabel: TextStyle
    get() = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )

val Typography.homeCountdownDigit: TextStyle
    get() = TextStyle(
        fontFamily = MontserratBold,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 35.sp,
    )

val Typography.homeEventLabel: TextStyle
    get() = TextStyle(
        fontFamily = MontserratLight,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    )

val Typography.homeEventValue: TextStyle
    get() = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )

val Typography.homeButtonLabel: TextStyle
    get() = TextStyle(
        fontFamily = MontserratMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )

val Typography.homeSectionLabel: TextStyle
    get() = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )

val Typography.homeSectionTitle: TextStyle
    get() = TextStyle(
        fontFamily = MontserratMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    )

val Typography.homeNoteBody: TextStyle
    get() = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    )

val Typography.homeLanguageLabel: TextStyle
    get() = TextStyle(
        fontFamily = MontserratMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )

// ── Award Detail screen typography tokens ───────────────────────────────────
private val MontserratSemiBold = FontFamily(
    Font(googleFont = montserratFont, fontProvider = googleFontProvider, weight = FontWeight.SemiBold)
)
private val MontserratExtraBold = FontFamily(
    Font(googleFont = montserratFont, fontProvider = googleFontProvider, weight = FontWeight.ExtraBold)
)

val Typography.awardDetailTitle: TextStyle
    get() = TextStyle(
        fontFamily = MontserratBold,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )

val Typography.awardDetailKudosTitle: TextStyle
    get() = TextStyle(
        fontFamily = MontserratExtraBold,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
    )

val Typography.awardDetailSectionTitle: TextStyle
    get() = TextStyle(
        fontFamily = MontserratBold,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    )

val Typography.awardDetailPrizeValue: TextStyle
    get() = TextStyle(
        fontFamily = MontserratBold,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    )

val Typography.awardDetailQuantityValue: TextStyle
    get() = TextStyle(
        fontFamily = MontserratBold,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    )

val Typography.awardDetailSectionLabel: TextStyle
    get() = TextStyle(
        fontFamily = MontserratSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )

val Typography.awardDetailBody: TextStyle
    get() = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    )

// ── Community Standards screen typography tokens ─────────────────────────────
val Typography.communityStandardsScreenTitle: TextStyle
    get() = TextStyle(
        fontFamily = MontserratBold,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    )

val Typography.communityStandardsSectionTitle: TextStyle
    get() = TextStyle(
        fontFamily = MontserratSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

val Typography.communityStandardsSectionBody: TextStyle
    get() = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )

// ── Base typography ─────────────────────────────────────────────────────────
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )
)
