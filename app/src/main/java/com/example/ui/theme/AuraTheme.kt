package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.DeckTheme

// Global AuraDeck Color Tokens
val AuraSpaceDark = Color(0xFF070A12)
val AuraSlateDark = Color(0xFF0F172A)
val AuraCardDark = Color(0xFF131D31)
val AuraGlassBorder = Color(0x35FFFFFF)
val AuraGlassHighlight = Color(0x60FFFFFF)

// Accent Neons
val AuraCyan = Color(0xFF00F0FF)
val AuraViolet = Color(0xFFA855F7)
val AuraIndigo = Color(0xFF6366F1)
val AuraEmerald = Color(0xFF10B981)
val AuraRose = Color(0xFFFF3366)
val AuraGold = Color(0xFFF59E0B)

val AuraTextPrimary = Color(0xFFF8FAFC)
val AuraTextSecondary = Color(0xFF94A3B8)
val AuraTextMuted = Color(0xFF64748B)

private val AuraDarkColorScheme = darkColorScheme(
    primary = AuraCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0E384D),
    onPrimaryContainer = AuraCyan,
    secondary = AuraViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3B1E63),
    onSecondaryContainer = Color(0xFFE9D5FF),
    tertiary = AuraEmerald,
    background = AuraSpaceDark,
    onBackground = AuraTextPrimary,
    surface = AuraSlateDark,
    onSurface = AuraTextPrimary,
    surfaceVariant = AuraCardDark,
    onSurfaceVariant = AuraTextSecondary
)

@Composable
fun AuraDeckTheme(
    deckTheme: DeckTheme = DeckTheme.AURORA_DARK,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AuraDarkColorScheme,
        typography = Typography,
        content = content
    )
}

fun parseColorHex(hex: String, default: Color = Color.White): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        when (cleanHex.length) {
            6 -> Color(android.graphics.Color.parseColor("#$cleanHex"))
            8 -> Color(android.graphics.Color.parseColor("#$cleanHex"))
            else -> default
        }
    } catch (_: Exception) {
        default
    }
}
