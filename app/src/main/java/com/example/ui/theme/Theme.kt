package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Custom M3 shapes mapping to the curved aesthetic of Sophisticated Dark
val VersaCareerShapes = Shapes(
  small = RoundedCornerShape(8.dp),
  medium = RoundedCornerShape(12.dp),
  large = RoundedCornerShape(24.dp),       // curved cards/containers (rounded-3xl)
  extraLarge = RoundedCornerShape(40.dp)   // premium master cards (rounded-[2.5rem])
)

private val VersaCareerColorScheme = darkColorScheme(
  primary = BrandPrimary,
  secondary = BrandSecondary,
  background = BrandBackground,
  surface = BrandCardBg,
  onPrimary = Color.White,
  onSecondary = Color(0xFF0F172A),
  onBackground = BrandText,
  onSurface = BrandText,
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = BrandSecondaryText,
  outline = AccentDarkBorder,
  primaryContainer = BrandPrimary.copy(alpha = 0.15f),
  onPrimaryContainer = BrandSecondary,
  secondaryContainer = BrandSecondary.copy(alpha = 0.15f),
  onSecondaryContainer = Color.White,
  error = AccentRed,
  onError = Color.White,
  surfaceBright = Color(0xFF243249),
  surfaceContainer = BrandCardBg
)

@Composable
fun VersaCareerTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = VersaCareerColorScheme,
    typography = Typography,
    shapes = VersaCareerShapes,
    content = content
  )
}

