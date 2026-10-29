package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Collaborator
import com.example.model.DeckTheme
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.parseColorHex

@Composable
fun AmbientAuroraCanvas(
    modifier: Modifier = Modifier,
    theme: DeckTheme = DeckTheme.AURORA_DARK,
    showGrid: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.70f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "aurora_alpha"
    )

    val primaryAccent = parseColorHex(theme.primaryAccentHex, AuraCyan)
    val secondaryAccent = parseColorHex(theme.secondaryAccentHex, Color(0xFFA855F7))
    val bgStart = parseColorHex(theme.backgroundStartHex, Color(0xFF070B14))
    val bgEnd = parseColorHex(theme.backgroundEndHex, Color(0xFF0E172A))

    Canvas(modifier = modifier.fillMaxSize()) {
        // Deep gradient base
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(bgStart, bgEnd)
            )
        )

        // Radial ambient glow #1 (Top Left)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryAccent.copy(alpha = 0.22f * pulseAlpha),
                    primaryAccent.copy(alpha = 0.08f * pulseAlpha),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.15f, size.height * 0.20f),
                radius = size.width * 0.60f
            ),
            radius = size.width * 0.60f,
            center = Offset(size.width * 0.15f, size.height * 0.20f)
        )

        // Radial ambient glow #2 (Bottom Right)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    secondaryAccent.copy(alpha = 0.20f * pulseAlpha),
                    secondaryAccent.copy(alpha = 0.05f * pulseAlpha),
                    Color.Transparent
                ),
                center = Offset(size.width * 0.85f, size.height * 0.75f),
                radius = size.width * 0.55f
            ),
            radius = size.width * 0.55f,
            center = Offset(size.width * 0.85f, size.height * 0.75f)
        )

        // Subtle fine grid pattern for professional design precision
        if (showGrid) {
            val step = 48.dp.toPx()
            var x = step
            while (x < size.width) {
                drawLine(
                    color = Color.White.copy(alpha = 0.035f),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += step
            }
            var y = step
            while (y < size.height) {
                drawLine(
                    color = Color.White.copy(alpha = 0.035f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += step
            }
        }
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = Color(0x241E293B),
    borderColor: Color = Color(0x35FFFFFF),
    borderWidth: Dp = 1.dp,
    elevation: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .shadow(elevation, shape)
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        backgroundColor.copy(alpha = 0.35f),
                        backgroundColor.copy(alpha = 0.18f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .border(
                border = BorderStroke(
                    borderWidth,
                    Brush.linearGradient(
                        colors = listOf(
                            borderColor.copy(alpha = 0.55f),
                            borderColor.copy(alpha = 0.15f),
                            borderColor.copy(alpha = 0.05f)
                        )
                    )
                ),
                shape = shape
            )
            .then(clickableModifier),
        content = content
    )
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color = AuraCyan,
    isPrimary: Boolean = false
) {
    val bgColors = if (isPrimary) {
        listOf(accentColor.copy(alpha = 0.28f), accentColor.copy(alpha = 0.12f))
    } else {
        listOf(Color(0x30FFFFFF), Color(0x10FFFFFF))
    }

    val borderColors = if (isPrimary) {
        listOf(accentColor.copy(alpha = 0.9f), accentColor.copy(alpha = 0.3f))
    } else {
        listOf(Color(0x50FFFFFF), Color(0x15FFFFFF))
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(borderColors)
                ),
                RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = if (isPrimary) accentColor else AuraTextPrimary
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isPrimary) accentColor else AuraTextPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isPrimary) accentColor else AuraTextPrimary
            )
        }
    }
}

@Composable
fun GlassBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AuraCyan,
    showDot: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.14f))
            .border(
                BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(
                        listOf(color.copy(alpha = 0.6f), color.copy(alpha = 0.15f))
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = color
            )
        }
    }
}

@Composable
fun PeerCursorBadge(
    collaborator: Collaborator,
    canvasWidthPx: Float,
    canvasHeightPx: Float,
    modifier: Modifier = Modifier
) {
    val cursorX = (collaborator.cursorXPercent * canvasWidthPx)
    val cursorY = (collaborator.cursorYPercent * canvasHeightPx)
    val color = parseColorHex(collaborator.colorHex, AuraCyan)

    Box(
        modifier = modifier
            .offset(
                x = cursorX.dp / 2.7f,
                y = cursorY.dp / 2.7f
            )
    ) {
        // Pointer icon & label
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pointer arrow shape
            Canvas(modifier = Modifier.size(14.dp)) {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, size.height * 0.7f)
                    lineTo(size.width * 0.45f, size.height * 0.7f)
                    lineTo(size.width * 0.35f, size.height)
                    close()
                }
                drawPath(path = path, color = color)
            }

            Spacer(modifier = Modifier.width(4.dp))

            // User name pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.90f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = collaborator.name.split(" ").firstOrNull() ?: collaborator.name,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun AlignmentGuideLines(
    showHorizontalCenter: Boolean,
    showVerticalCenter: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (showVerticalCenter) {
            drawLine(
                color = AuraCyan.copy(alpha = 0.8f),
                start = Offset(size.width / 2f, 0f),
                end = Offset(size.width / 2f, size.height),
                strokeWidth = 2f
            )
        }
        if (showHorizontalCenter) {
            drawLine(
                color = AuraCyan.copy(alpha = 0.8f),
                start = Offset(0f, size.height / 2f),
                end = Offset(size.width, size.height / 2f),
                strokeWidth = 2f
            )
        }
    }
}
