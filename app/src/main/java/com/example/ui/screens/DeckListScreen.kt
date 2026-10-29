package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SlideDeck
import com.example.ui.components.AmbientAuroraCanvas
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.AuraCardDark
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraRose
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.parseColorHex
import com.example.viewmodel.DeckViewModel

@Composable
fun DeckListScreen(
    viewModel: DeckViewModel,
    onOpenDeck: (String) -> Unit
) {
    val decks by viewModel.allDecks.collectAsState()
    val isAiGeneratorDialogOpen by viewModel.isAiGeneratorDialogOpen.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
    ) {
        // Subtle ambient canvas
        AmbientAuroraCanvas(showGrid = true)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO HEADER
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(AuraCyan.copy(alpha = 0.35f), Color(0xFFA855F7).copy(alpha = 0.35f))
                                    )
                                )
                                .border(1.dp, AuraCyan.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Slideshow,
                                contentDescription = null,
                                tint = AuraCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "AuraDeck",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AuraTextPrimary,
                                    letterSpacing = (-0.5).sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                GlassBadge(
                                    text = "AI ENGINE",
                                    color = AuraCyan,
                                    showDot = true
                                )
                            }
                            Text(
                                text = "Professional presentation studio & real-time canvas",
                                fontSize = 12.sp,
                                color = AuraTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // HERO CTA BANNER
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0x351E293B),
                        borderColor = AuraCyan.copy(alpha = 0.4f),
                        onClick = { viewModel.openAiGeneratorDialog(true) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Generate New Deck with AI",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AuraTextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Turn any prompt or concept into high-impact glassmorphic slide decks in seconds.",
                                    fontSize = 12.sp,
                                    color = AuraTextSecondary,
                                    lineHeight = 17.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(AuraCyan, Color(0xFFA855F7))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Generate",
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // SECTION TITLE
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "PRESENTATION DECKS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${decks.size} Decks Active",
                        fontSize = 12.sp,
                        color = AuraCyan
                    )
                }
            }

            // DECKS LIST
            items(decks, key = { it.id }) { deckItem ->
                DeckListItemCard(
                    deck = deckItem,
                    onOpen = { onOpenDeck(deckItem.id) },
                    onPresent = {
                        viewModel.selectDeck(deckItem.id)
                        viewModel.togglePresentationMode()
                    },
                    onDuplicate = { viewModel.duplicateDeck(deckItem.id) },
                    onDelete = { viewModel.deleteDeck(deckItem.id) }
                )
            }
        }
    }

    // AI GENERATOR DIALOG
    if (isAiGeneratorDialogOpen) {
        AiGeneratorDialog(
            onDismiss = { viewModel.openAiGeneratorDialog(false) },
            onGenerate = { topic, industry, tone, count, theme ->
                viewModel.generateNewDeck(topic, industry, tone, count, theme)
            }
        )
    }
}

@Composable
fun DeckListItemCard(
    deck: SlideDeck,
    onOpen: () -> Unit,
    onPresent: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    val themeColor = parseColorHex(deck.theme.primaryAccentHex, AuraCyan)
    val firstSlide = deck.slides.firstOrNull()

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color(0x28131D31),
        borderColor = Color(0x30FFFFFF),
        onClick = onOpen
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Deck Preview Top
            Row(verticalAlignment = Alignment.Top) {
                // Mini 16:9 Thumbnail
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF090E1A))
                        .border(1.dp, themeColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(themeColor)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = firstSlide?.title ?: "Slide 1",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuraTextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = firstSlide?.category ?: "Keynote",
                            fontSize = 7.sp,
                            color = AuraTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title & Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = deck.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = deck.subtitle.ifEmpty { "Professional Presentation Deck" },
                        fontSize = 11.sp,
                        color = AuraTextSecondary,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GlassBadge(
                            text = "${deck.slides.size} Slides",
                            color = themeColor,
                            showDot = false
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        GlassBadge(
                            text = deck.theme.displayName,
                            color = Color(0xFFA855F7),
                            showDot = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Present Button
                GlassButton(
                    text = "Present",
                    icon = Icons.Default.PlayArrow,
                    isPrimary = true,
                    onClick = onPresent,
                    accentColor = themeColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Edit Button
                GlassButton(
                    text = "Edit Canvas",
                    onClick = onOpen
                )

                Spacer(modifier = Modifier.weight(1f))

                // Duplicate
                IconButton(
                    onClick = onDuplicate,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Duplicate Deck",
                        tint = AuraTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Delete
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Deck",
                        tint = AuraRose.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
