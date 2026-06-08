package com.dario.kmp.feature.discover.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Discover feature typography.
 *
 * To use GT Walsheim, copy the .otf files into
 * `shared/src/commonMain/composeResources/font/` then replace
 * [FontFamily.Default] with:
 * ```kotlin
 * FontFamily(
 *     Font(Res.font.gt_walsheim_regular, FontWeight.Normal),
 *     Font(Res.font.gt_walsheim_medium,  FontWeight.Medium),
 *     Font(Res.font.gt_walsheim_bold,    FontWeight.Bold),
 * )
 * ```
 */
internal fun discoverTypography(): Typography {
    val fontFamily = FontFamily.Default  // swap for GT Walsheim once font files are added
    val base = TextStyle(fontFamily = fontFamily)
    return Typography(
        displayLarge   = base.copy(fontWeight = FontWeight.Bold,     fontSize = 57.sp),
        displayMedium  = base.copy(fontWeight = FontWeight.Bold,     fontSize = 45.sp),
        displaySmall   = base.copy(fontWeight = FontWeight.Bold,     fontSize = 36.sp),
        headlineLarge  = base.copy(fontWeight = FontWeight.Bold,     fontSize = 32.sp),
        headlineMedium = base.copy(fontWeight = FontWeight.Bold,     fontSize = 28.sp),
        headlineSmall  = base.copy(fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
        titleLarge     = base.copy(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
        titleMedium    = base.copy(fontWeight = FontWeight.Medium,   fontSize = 16.sp),
        titleSmall     = base.copy(fontWeight = FontWeight.Medium,   fontSize = 14.sp),
        bodyLarge      = base.copy(fontWeight = FontWeight.Normal,   fontSize = 16.sp),
        bodyMedium     = base.copy(fontWeight = FontWeight.Normal,   fontSize = 14.sp),
        bodySmall      = base.copy(fontWeight = FontWeight.Normal,   fontSize = 12.sp),
        labelLarge     = base.copy(fontWeight = FontWeight.Medium,   fontSize = 14.sp),
        labelMedium    = base.copy(fontWeight = FontWeight.Medium,   fontSize = 12.sp),
        labelSmall     = base.copy(fontWeight = FontWeight.Medium,   fontSize = 11.sp),
    )
}
