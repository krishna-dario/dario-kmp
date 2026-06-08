package com.dario.kmp.discover.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Discover feature typography.
 *
 * To use the GT Walsheim custom font, copy the following .otf files from the Android module
 * into shared/src/commonMain/composeResources/font/ :
 *   - gt_walsheim_regular.otf
 *   - gt_walsheim_medium.otf
 *   - gt_walsheim_bold.otf
 *
 * Then replace [FontFamily.Default] below with:
 *   FontFamily(
 *       Font(Res.font.gt_walsheim_regular, FontWeight.Normal),
 *       Font(Res.font.gt_walsheim_medium, FontWeight.Medium),
 *       Font(Res.font.gt_walsheim_bold,   FontWeight.Bold),
 *   )
 *
 * The rest of the typography spec is identical to the Android module.
 */
@Composable
fun discoverTypography(): Typography {
    // Swap FontFamily.Default for the custom font once font files are added.
    val fontFamily = FontFamily.Default

    val base = TextStyle(fontFamily = fontFamily)

    return Typography(
        displayLarge  = base.copy(fontWeight = FontWeight.Bold,     fontSize = 57.sp),
        displayMedium = base.copy(fontWeight = FontWeight.Bold,     fontSize = 45.sp),
        displaySmall  = base.copy(fontWeight = FontWeight.Bold,     fontSize = 36.sp),
        headlineLarge = base.copy(fontWeight = FontWeight.Bold,     fontSize = 32.sp),
        headlineMedium= base.copy(fontWeight = FontWeight.Bold,     fontSize = 28.sp),
        headlineSmall = base.copy(fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
        titleLarge    = base.copy(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
        titleMedium   = base.copy(fontWeight = FontWeight.Medium,   fontSize = 16.sp),
        titleSmall    = base.copy(fontWeight = FontWeight.Medium,   fontSize = 14.sp),
        bodyLarge     = base.copy(fontWeight = FontWeight.Normal,   fontSize = 16.sp),
        bodyMedium    = base.copy(fontWeight = FontWeight.Normal,   fontSize = 14.sp),
        bodySmall     = base.copy(fontWeight = FontWeight.Normal,   fontSize = 12.sp),
        labelLarge    = base.copy(fontWeight = FontWeight.Medium,   fontSize = 14.sp),
        labelMedium   = base.copy(fontWeight = FontWeight.Medium,   fontSize = 12.sp),
        labelSmall    = base.copy(fontWeight = FontWeight.Medium,   fontSize = 11.sp),
    )
}

/**
 * Wraps content in [MaterialTheme] with Discover typography applied.
 */
@Composable
fun DiscoverTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = discoverTypography(),
        content = content
    )
}
