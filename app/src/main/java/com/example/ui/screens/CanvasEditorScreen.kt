package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Collaborator
import com.example.model.DeckTheme
import com.example.model.ElementType
import com.example.model.LayoutPreset
import com.example.model.Slide
import com.example.model.SlideDeck
import com.example.model.SlideElement
import com.example.ui.components.AlignmentGuideLines
import com.example.ui.components.AmbientAuroraCanvas
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.PeerCursorBadge
import com.example.ui.theme.AuraCardDark
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.AuraViolet
import com.example.ui.theme.parseColorHex
import com.example.viewmodel.DeckViewModel
import kotlin.math.roundToInt

@Composable
fun CanvasEditorScreen(
    viewModel: DeckViewModel,
    onBackToDecks: () -> Unit
) {
    BackHandler { onBackToDecks() }

    val deck by viewModel.currentDeck.collectAsState()
    val currentSlideIndex by viewModel.currentSlideIndex.collectAsState()
    val currentSlide by viewModel.currentSlide.collectAsState()
    val selectedElementId by viewModel.selectedElementId.collectAsState()
    val collaborators by viewModel.collaborators.collectAsState()
    val liveActivity by viewModel.liveActivity.collectAsState(initial = "Collaboration engine active")
    val showPeerCursors by viewModel.showPeerCursors.collectAsState()
    val isNotesSheetOpen by viewModel.isNotesSheetOpen.collectAsState()
    val isElementEditorOpen by viewModel.isElementEditorOpen.collectAsState()
    val showHorizontalGuide by viewModel.showHorizontalGuide.collectAsState()
    val showVerticalGuide by viewModel.showVerticalGuide.collectAsState()

    var showThemeMenu by remember { mutableStateOf(false) }
    var showAddElementMenu by remember { mutableStateOf(false) }
    var showLayoutPresetsMenu by remember { mutableStateOf(false) }

    if (deck == null || currentSlide == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading slide deck...", color = AuraTextSecondary)
        }
        return
    }

    val activeDeck = deck!!
    val activeSlide = currentSlide!!
    val selectedElement = activeSlide.elements.firstOrNull { it.id == selectedElementId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070A12))
    ) {
        // TOP TOOLBAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            // Back button
            IconButton(
                onClick = onBackToDecks,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Decks",
                    tint = AuraTextPrimary
                )
            }

            // Deck Title with Theme Dropdown
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showThemeMenu = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Column {
                        Text(
                            text = activeDeck.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary,
                            maxLines = 1
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(parseColorHex(activeDeck.theme.primaryAccentHex, AuraCyan))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = activeDeck.theme.displayName,
                                fontSize = 11.sp,
                                color = AuraTextSecondary
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = showThemeMenu,
                    onDismissRequest = { showThemeMenu = false },
                    modifier = Modifier.background(AuraCardDark)
                ) {
                    DeckTheme.values().forEach { theme ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(parseColorHex(theme.primaryAccentHex, AuraCyan))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = theme.displayName,
                                        color = if (activeDeck.theme == theme) AuraCyan else AuraTextPrimary,
                                        fontSize = 13.sp
                                    )
                                }
                            },
                            onClick = {
                                viewModel.setDeckTheme(theme)
                                showThemeMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Undo / Redo
            IconButton(
                onClick = { viewModel.undo() },
                enabled = viewModel.canUndo(),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    contentDescription = "Undo",
                    tint = if (viewModel.canUndo()) AuraTextPrimary else AuraTextMuted
                )
            }

            IconButton(
                onClick = { viewModel.redo() },
                enabled = viewModel.canRedo(),
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Redo,
                    contentDescription = "Redo",
                    tint = if (viewModel.canRedo()) AuraTextPrimary else AuraTextMuted
                )
            }

            // Collaborate / Invite
            IconButton(
                onClick = { viewModel.openCollaborationDialog(true) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Collaboration",
                    tint = AuraCyan
                )
            }

            // Present Fullscreen
            GlassButton(
                text = "Present",
                icon = Icons.Default.PlayArrow,
                isPrimary = true,
                onClick = { viewModel.togglePresentationMode() },
                accentColor = AuraCyan
            )
        }

        // COLLABORATION PRESENCE & LIVE ACTIVITY TICKER
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp)
        ) {
            // Collaborator Avatars Row
            collaborators.take(3).forEach { peer ->
                val peerColor = parseColorHex(peer.colorHex, AuraCyan)
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(peerColor.copy(alpha = 0.35f))
                        .border(1.dp, peerColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = peer.initials,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )
                }
            }

            // Toggle Peer Cursors
            IconButton(
                onClick = { viewModel.togglePeerCursors() },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (showPeerCursors) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Cursors",
                    tint = if (showPeerCursors) AuraCyan else AuraTextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Live Activity ticker
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x18FFFFFF))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = liveActivity,
                    fontSize = 11.sp,
                    color = AuraTextSecondary,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // INTERACTIVE SLIDE CANVAS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF070B14))
                    .border(
                        BorderStroke(
                            1.dp,
                            Brush.linearGradient(
                                listOf(Color(0x50FFFFFF), Color(0x15FFFFFF), Color(0x05FFFFFF))
                            )
                        ),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { viewModel.selectElement(null) }
            ) {
                val canvasWidth = maxWidth
                val canvasHeight = maxHeight
                val canvasWidthPx = constraints.maxWidth.toFloat()
                val canvasHeightPx = constraints.maxHeight.toFloat()

                // Ambient Backdrop Canvas
                AmbientAuroraCanvas(theme = activeDeck.theme, showGrid = true)

                // Snapping guidelines
                AlignmentGuideLines(
                    showHorizontalCenter = showHorizontalGuide,
                    showVerticalCenter = showVerticalGuide
                )

                // Render Slide Elements with Drag-and-Drop
                activeSlide.elements.forEach { element ->
                    val isSelected = element.id == selectedElementId

                    InteractiveElementView(
                        element = element,
                        isSelected = isSelected,
                        theme = activeDeck.theme,
                        canvasWidth = canvasWidth,
                        canvasHeight = canvasHeight,
                        onSelect = { viewModel.selectElement(element.id) },
                        onDrag = { dxPercent, dyPercent ->
                            val currentX = element.xPercent
                            val currentY = element.yPercent
                            viewModel.updateElementPosition(
                                element.id,
                                currentX + dxPercent,
                                currentY + dyPercent
                            )
                        },
                        onDragEnd = { viewModel.clearGuides() }
                    )
                }

                // Render Live Peer Cursors
                if (showPeerCursors) {
                    collaborators.forEach { collaborator ->
                        PeerCursorBadge(
                            collaborator = collaborator,
                            canvasWidthPx = canvasWidthPx,
                            canvasHeightPx = canvasHeightPx
                        )
                    }
                }

                // Selected Element Floating Quick Actions
                if (selectedElement != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xCC0F172A))
                            .border(1.dp, AuraCyan.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // AI Polish
                            IconButton(
                                onClick = { viewModel.polishSelectedElementText("punchy") },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI Polish",
                                    tint = AuraCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Edit Text
                            IconButton(
                                onClick = { viewModel.openElementEditor(true) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Text",
                                    tint = AuraTextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Duplicate
                            IconButton(
                                onClick = { viewModel.duplicateSelectedElement() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Duplicate",
                                    tint = AuraTextPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Delete
                            IconButton(
                                onClick = { viewModel.deleteSelectedElement() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = AuraRose,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // AI ASSISTANT & CANVAS TOOLS FLOATING BAR
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0x25FFFFFF))
                .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            // AI Magic Re-Layout button
            GlassButton(
                text = "AI Re-Layout",
                icon = Icons.Default.AutoAwesome,
                isPrimary = true,
                onClick = { viewModel.autoSmartReLayout() },
                accentColor = AuraCyan
            )

            // Layout Presets Picker
            Box {
                GlassButton(
                    text = "Layouts",
                    icon = Icons.Default.Layers,
                    onClick = { showLayoutPresetsMenu = true }
                )

                DropdownMenu(
                    expanded = showLayoutPresetsMenu,
                    onDismissRequest = { showLayoutPresetsMenu = false },
                    modifier = Modifier.background(AuraCardDark)
                ) {
                    LayoutPreset.values().forEach { preset ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = preset.title,
                                        color = if (activeSlide.layoutPreset == preset) AuraCyan else AuraTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = preset.description,
                                        color = AuraTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            },
                            onClick = {
                                viewModel.applyLayoutPreset(preset)
                                showLayoutPresetsMenu = false
                            }
                        )
                    }
                }
            }

            // Add Element Dropdown
            Box {
                GlassButton(
                    text = "Add",
                    icon = Icons.Default.Add,
                    onClick = { showAddElementMenu = true }
                )

                DropdownMenu(
                    expanded = showAddElementMenu,
                    onDismissRequest = { showAddElementMenu = false },
                    modifier = Modifier.background(AuraCardDark)
                ) {
                    DropdownMenuItem(
                        text = { Text("Heading / Title", color = AuraTextPrimary) },
                        onClick = {
                            viewModel.addElement(ElementType.HEADING)
                            showAddElementMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Metric Stat Card", color = AuraCyan) },
                        onClick = {
                            viewModel.addElement(ElementType.METRIC_CARD)
                            showAddElementMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Glass Container", color = AuraTextPrimary) },
                        onClick = {
                            viewModel.addElement(ElementType.GLASS_CONTAINER)
                            showAddElementMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Status Badge", color = AuraEmerald) },
                        onClick = {
                            viewModel.addElement(ElementType.BADGE)
                            showAddElementMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sticky Note (Team)", color = Color(0xFFF59E0B)) },
                        onClick = {
                            viewModel.addElement(ElementType.STICKY_NOTE)
                            showAddElementMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Quote / Testimonial", color = AuraViolet) },
                        onClick = {
                            viewModel.addElement(ElementType.QUOTE)
                            showAddElementMenu = false
                        }
                    )
                }
            }

            // Presenter Notes Toggle
            IconButton(
                onClick = { viewModel.openNotesSheet(!isNotesSheetOpen) },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isNotesSheetOpen) AuraCyan.copy(alpha = 0.25f) else Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Notes,
                    contentDescription = "Presenter Notes",
                    tint = if (isNotesSheetOpen) AuraCyan else AuraTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // BOTTOM SLIDE STRIP CAROUSEL
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "SLIDES (${activeDeck.slides.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuraTextSecondary,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Delete current slide button (if more than 1 slide)
                if (activeDeck.slides.size > 1) {
                    Text(
                        text = "Delete Slide",
                        fontSize = 11.sp,
                        color = AuraRose,
                        modifier = Modifier
                            .clickable { viewModel.deleteSlide(currentSlideIndex) }
                            .padding(4.dp)
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(activeDeck.slides) { idx, slideItem ->
                    val isCurrent = idx == currentSlideIndex

                    Box(
                        modifier = Modifier
                            .width(88.dp)
                            .height(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .border(
                                width = if (isCurrent) 2.dp else 1.dp,
                                color = if (isCurrent) AuraCyan else Color(0x30FFFFFF),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.selectSlide(idx) }
                            .padding(6.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(if (isCurrent) AuraCyan else Color(0x40FFFFFF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${idx + 1}",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color.Black else AuraTextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = slideItem.category,
                                    fontSize = 9.sp,
                                    color = AuraTextSecondary,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = slideItem.title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AuraTextPrimary,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Add Slide button
                item {
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x15FFFFFF))
                            .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(8.dp))
                            .clickable { viewModel.addSlide() },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Slide",
                                tint = AuraCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "New",
                                fontSize = 9.sp,
                                color = AuraTextPrimary
                            )
                        }
                    }
                }
            }
        }
    }

    // PRESENTER NOTES DRAWER
    AnimatedVisibility(
        visible = isNotesSheetOpen,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(AuraCardDark.copy(alpha = 0.98f))
                .border(
                    BorderStroke(1.dp, Color(0x40FFFFFF)),
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(14.dp)
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
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Presenter Talking Points & Cues",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = { viewModel.openNotesSheet(false) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = AuraTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = activeSlide.presenterNotes.ifEmpty { "No presenter notes yet. Tap AI Re-Layout to generate talking cues." },
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = AuraTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "AI Rationale: ${activeSlide.aiLayoutRationale}",
                    fontSize = 11.sp,
                    color = AuraCyan
                )
            }
        }
    }

    // ELEMENT EDITOR DIALOG
    if (isElementEditorOpen && selectedElement != null) {
        ElementEditorDialog(
            element = selectedElement,
            onDismiss = { viewModel.openElementEditor(false) },
            onSave = { content, secondary ->
                viewModel.updateElementContent(selectedElement.id, content, secondary)
            },
            onAiPolish = { tone ->
                viewModel.polishSelectedElementText(tone)
            }
        )
    }
}

@Composable
fun InteractiveElementView(
    element: SlideElement,
    isSelected: Boolean,
    theme: DeckTheme,
    canvasWidth: androidx.compose.ui.unit.Dp,
    canvasHeight: androidx.compose.ui.unit.Dp,
    onSelect: () -> Unit,
    onDrag: (dxPercent: Float, dyPercent: Float) -> Unit,
    onDragEnd: () -> Unit
) {
    val accentColor = element.accentColorHex?.let { parseColorHex(it, AuraCyan) }
        ?: parseColorHex(theme.primaryAccentHex, AuraCyan)

    val elX = canvasWidth * element.xPercent
    val elY = canvasHeight * element.yPercent
    val elW = canvasWidth * element.widthPercent
    val elH = canvasHeight * element.heightPercent

    Box(
        modifier = Modifier
            .offset(x = elX, y = elY)
            .size(width = elW, height = elH)
            .pointerInput(element.id) {
                detectDragGestures(
                    onDragStart = { onSelect() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val dxPercent = dragAmount.x / size.width * element.widthPercent
                        val dyPercent = dragAmount.y / size.height * element.heightPercent
                        onDrag(dxPercent, dyPercent)
                    },
                    onDragEnd = onDragEnd,
                    onDragCancel = onDragEnd
                )
            }
            .clickable { onSelect() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        BorderStroke(1.5.dp, AuraCyan),
                        RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .padding(2.dp)
    ) {
        when (element.type) {
            ElementType.HEADING -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = element.content,
                        fontSize = (element.fontSizeSp * 0.70f).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = parseColorHex(theme.textColorHex, AuraTextPrimary),
                        lineHeight = (element.fontSizeSp * 0.90f).sp
                    )
                    if (element.secondaryContent.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = element.secondaryContent,
                            fontSize = (element.fontSizeSp * 0.42f).sp,
                            color = parseColorHex(theme.subtitleColorHex, AuraTextSecondary),
                            lineHeight = (element.fontSizeSp * 0.60f).sp
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
                    backgroundColor = Color(0x351E293B),
                    borderColor = accentColor.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        if (element.trendValue.isNotBlank()) {
                            GlassBadge(
                                text = element.trendValue,
                                color = accentColor,
                                showDot = false
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 1.15f).sp,
                            fontWeight = FontWeight.Black,
                            color = accentColor
                        )
                        Text(
                            text = element.secondaryContent,
                            fontSize = (element.fontSizeSp * 0.42f).sp,
                            color = AuraTextSecondary,
                            maxLines = 2
                        )
                    }
                }
            }

            ElementType.STICKY_NOTE -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x55F59E0B))
                        .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF59E0B))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "TEAM NOTE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFDE68A)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = element.content,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                    }
                }
            }

            ElementType.TIMELINE_STEP -> {
                GlassCard(
                    backgroundColor = Color(0x301E293B),
                    borderColor = accentColor.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        if (element.badgeLabel.isNotBlank()) {
                            GlassBadge(
                                text = element.badgeLabel,
                                color = accentColor,
                                showDot = true
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 0.65f).sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                        Text(
                            text = element.secondaryContent,
                            fontSize = (element.fontSizeSp * 0.42f).sp,
                            color = AuraTextSecondary,
                            maxLines = 2
                        )
                    }
                }
            }

            ElementType.QUOTE -> {
                GlassCard(
                    backgroundColor = Color(0x281E293B),
                    borderColor = Color(0x408B5CF6),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 0.65f).sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = AuraTextPrimary,
                            maxLines = 2
                        )
                        if (element.secondaryContent.isNotBlank()) {
                            Text(
                                text = element.secondaryContent,
                                fontSize = (element.fontSizeSp * 0.45f).sp,
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
                            .padding(8.dp)
                    ) {
                        if (element.badgeLabel.isNotBlank()) {
                            GlassBadge(
                                text = element.badgeLabel,
                                color = accentColor,
                                showDot = false
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        Text(
                            text = element.content,
                            fontSize = (element.fontSizeSp * 0.65f).sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary
                        )
                        if (element.secondaryContent.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = element.secondaryContent,
                                fontSize = (element.fontSizeSp * 0.42f).sp,
                                color = AuraTextSecondary,
                                lineHeight = (element.fontSizeSp * 0.60f).sp,
                                maxLines = 4
                            )
                        }
                    }
                }
            }
        }
    }
}
