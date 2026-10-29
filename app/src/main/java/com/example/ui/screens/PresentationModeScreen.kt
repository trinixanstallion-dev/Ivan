package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeckTheme
import com.example.model.ElementType
import com.example.model.Slide
import com.example.model.SlideDeck
import com.example.model.SlideElement
import com.example.ui.components.AmbientAuroraCanvas
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassCard
import com.example.ui.theme.AuraCardDark
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.parseColorHex
import kotlinx.coroutines.delay

@Composable
fun PresentationModeScreen(
    deck: SlideDeck,
    currentSlideIndex: Int,
    onSelectSlide: (Int) -> Unit,
    onExit: () -> Unit
) {
    BackHandler { onExit() }

    val currentSlide = deck.slides.getOrNull(currentSlideIndex) ?: return
    var isLaserActive by remember { mutableStateOf(false) }
    var laserPos by remember { mutableStateOf<Offset?>(null) }
    var laserTrail by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var isNotesOpen by remember { mutableStateOf(false) }

    // Presentation timer in seconds
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedSeconds++
        }
    }

    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timerText = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Slide Canvas Container
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            contentAlignment = Alignment.Center
        ) {
            val canvasWidth = maxWidth
            val canvasHeight = maxHeight

            Box(
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF070B14))
            ) {
                // Background
                AmbientAuroraCanvas(theme = deck.theme, showGrid = false)

                // Render Slide Elements
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val slideW = maxWidth
                    val slideH = maxHeight

                    currentSlide.elements.forEach { el ->
                        PresentationElementView(
                            element = el,
                            theme = deck.theme,
                            modifier = Modifier
                                .offset(
                                    x = slideW * el.xPercent,
                                    y = slideH * el.yPercent
                                )
                                .size(
                                    width = slideW * el.widthPercent,
                                    height = slideH * el.heightPercent
                                )
                        )
                    }

                    // Laser pointer canvas overlay
                    if (isLaserActive) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            laserPos = offset
                                            laserTrail = listOf(offset)
                                        },
                                        onDrag = { change, _ ->
                                            laserPos = change.position
                                            laserTrail = (laserTrail + change.position).takeLast(10)
                                        },
                                        onDragEnd = {
                                            laserPos = null
                                            laserTrail = emptyList()
                                        },
                                        onDragCancel = {
                                            laserPos = null
                                            laserTrail = emptyList()
                                        }
                                    )
                                }
                        ) {
                            // Draw glowing laser trail
                            laserTrail.forEachIndexed { idx, point ->
                                val alpha = (idx + 1).toFloat() / laserTrail.size
                                drawCircle(
                                    color = AuraRose.copy(alpha = alpha * 0.4f),
                                    radius = 8.dp.toPx() * alpha,
                                    center = point
                                )
                            }

                            // Draw central laser core
                            laserPos?.let { pos ->
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White,
                                            AuraRose,
                                            AuraRose.copy(alpha = 0.5f),
                                            Color.Transparent
                                        ),
                                        center = pos,
                                        radius = 20.dp.toPx()
                                    ),
                                    radius = 20.dp.toPx(),
                                    center = pos
                                )
                                drawCircle(
                                    color = Color.White,
                                    radius = 4.dp.toPx(),
                                    center = pos
                                )
                            }
                        }
                    }
                }
            }
        }

        // Top HUD Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Deck Title & Slide Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x60000000))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = deck.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuraTextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                GlassBadge(
                    text = "${currentSlideIndex + 1} / ${deck.slides.size}",
                    color = AuraCyan,
                    showDot = false
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Timer Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x60000000))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = AuraCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = timerText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuraCyan
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Close button
            IconButton(
                onClick = onExit,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x60000000))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit Presentation",
                    tint = AuraTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Presenter Notes Bottom Drawer
        AnimatedVisibility(
            visible = isNotesOpen,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(AuraCardDark.copy(alpha = 0.95f))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notes,
                            contentDescription = null,
                            tint = AuraCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Speaker Notes & Talking Points",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { isNotesOpen = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Notes",
                                tint = AuraTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentSlide.presenterNotes.ifEmpty { "No speaker notes written for this slide yet. Tap AI Magic Re-Layout or Edit to generate cues." },
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = AuraTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "AI Design Rationale: ${currentSlide.aiLayoutRationale}",
                        fontSize = 11.sp,
                        color = AuraCyan
                    )
                }
            }
        }

        // Floating Bottom Control Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0x80000000))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            // Previous Slide
            IconButton(
                onClick = {
                    if (currentSlideIndex > 0) onSelectSlide(currentSlideIndex - 1)
                },
                enabled = currentSlideIndex > 0,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous Slide",
                    tint = if (currentSlideIndex > 0) AuraTextPrimary else AuraTextMuted
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Laser Pointer Toggle
            IconButton(
                onClick = { isLaserActive = !isLaserActive },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isLaserActive) AuraRose.copy(alpha = 0.3f) else Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Flare,
                    contentDescription = "Laser Pointer",
                    tint = if (isLaserActive) AuraRose else AuraTextSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Speaker Notes Toggle
            IconButton(
                onClick = { isNotesOpen = !isNotesOpen },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isNotesOpen) AuraCyan.copy(alpha = 0.3f) else Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Notes,
                    contentDescription = "Speaker Notes",
                    tint = if (isNotesOpen) AuraCyan else AuraTextSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Next Slide
            IconButton(
                onClick = {
                    if (currentSlideIndex < deck.slides.lastIndex) onSelectSlide(currentSlideIndex + 1)
                },
                enabled = currentSlideIndex < deck.slides.lastIndex,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Slide",
                    tint = if (currentSlideIndex < deck.slides.lastIndex) AuraTextPrimary else AuraTextMuted
                )
            }
        }
    }
}

@Composable
fun PresentationElementView(
    element: SlideElement,
    theme: DeckTheme,
    modifier: Modifier = Modifier
) {
    val accentColor = element.accentColorHex?.let { parseColorHex(it, AuraCyan) }
        ?: parseColorHex(theme.primaryAccentHex, AuraCyan)

    Box(modifier = modifier.padding(4.dp)) {
        when (element.type) {
            ElementType.HEADING -> {
                Column {
                    Text(
                        text = element.content,
                        fontSize = (element.fontSizeSp * 0.75f).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = parseColorHex(theme.textColorHex, AuraTextPrimary),
                        lineHeight = (element.fontSizeSp * 0.95f).sp
                    )
                    if (element.secondaryContent.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = element.secondaryContent,
                            fontSize = (element.fontSizeSp * 0.45f).sp,
                            color = parseColorHex(theme.subtitleColorHex, AuraTextSecondary),
                            lineHeight = (element.fontSizeSp * 0.65f).sp
                        )
                    }
                }
            }

            ElementType.BADGE -> {
                GlassBadge(
                    text = element.content,
                    color = accentColor,
                    showDot = true
                )
            }

            ElementType.METRIC_CARD -> {
                GlassCard(
                    backgroundColor = Color(0x301E293B),
                    borderColor = accentColor.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        if (element.trendValue.isNotBlank()) {
                            GlassBadge(
                                text = element.trendValue,
                                color = accentColor,
                                showDot = false
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 1.3f).sp,
                            fontWeight = FontWeight.Black,
                            color = accentColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = element.secondaryContent,
                            fontSize = (element.fontSizeSp * 0.48f).sp,
                            color = AuraTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            ElementType.TIMELINE_STEP -> {
                GlassCard(
                    backgroundColor = Color(0x281E293B),
                    borderColor = accentColor.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        if (element.badgeLabel.isNotBlank()) {
                            GlassBadge(
                                text = element.badgeLabel,
                                color = accentColor,
                                showDot = true
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 0.70f).sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = element.secondaryContent,
                            fontSize = (element.fontSizeSp * 0.45f).sp,
                            color = AuraTextSecondary
                        )
                    }
                }
            }

            ElementType.QUOTE -> {
                GlassCard(
                    backgroundColor = Color(0x251E293B),
                    borderColor = Color(0x408B5CF6),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 0.75f).sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = AuraTextPrimary
                        )
                        if (element.secondaryContent.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = element.secondaryContent,
                                fontSize = (element.fontSizeSp * 0.50f).sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }
                    }
                }
            }

            else -> {
                GlassCard(
                    backgroundColor = Color(0x281E293B),
                    borderColor = Color(0x30FFFFFF),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        if (element.badgeLabel.isNotBlank()) {
                            GlassBadge(
                                text = element.badgeLabel,
                                color = accentColor,
                                showDot = false
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 0.70f).sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                        if (element.secondaryContent.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = element.secondaryContent,
                                fontSize = (element.fontSizeSp * 0.46f).sp,
                                color = AuraTextSecondary,
                                lineHeight = (element.fontSizeSp * 0.65f).sp
                            )
                        }
                    }
                }
            }
        }
    }
}
