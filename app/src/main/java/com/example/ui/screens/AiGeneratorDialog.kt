package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeckTheme
import com.example.ui.components.GlassButton
import com.example.ui.theme.AuraCardDark
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.parseColorHex

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiGeneratorDialog(
    onDismiss: () -> Unit,
    onGenerate: (topic: String, industry: String, tone: String, slideCount: Int, theme: DeckTheme) -> Unit
) {
    var topic by remember { mutableStateOf("") }
    var selectedIndustry by remember { mutableStateOf("Artificial Intelligence") }
    var selectedTone by remember { mutableStateOf("Visionary Keynote") }
    var slideCount by remember { mutableFloatStateOf(5f) }
    var selectedTheme by remember { mutableStateOf(DeckTheme.AURORA_DARK) }

    val sampleTopics = listOf(
        "Autonomous Agent Orchestration Platform",
        "Q3 Board Strategy & Global Expansion",
        "Next-Gen Carbon Removal Architecture",
        "Quantum Computing for Drug Discovery",
        "Series A Pitch: Developer AI Copilot"
    )

    val industries = listOf(
        "Artificial Intelligence",
        "Enterprise SaaS",
        "Fintech & Crypto",
        "Deeptech & Robotics",
        "Climate Tech"
    )

    val tones = listOf(
        "Visionary Keynote",
        "Boardroom Executive",
        "Hard Data & Traction",
        "Fast-Paced Startup Pitch"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AuraCardDark,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(AuraCyan.copy(alpha = 0.3f), Color(0xFFA855F7).copy(alpha = 0.3f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AuraCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "AI Deck Generator",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Generates complete structured presentation decks",
                        fontSize = 12.sp,
                        color = AuraTextSecondary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Presentation Topic or Concept",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraTextPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    placeholder = {
                        Text(
                            text = "e.g. Next-Gen Enterprise Platform",
                            color = AuraTextMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = AuraTextPrimary,
                        unfocusedTextColor = AuraTextPrimary,
                        focusedBorderColor = AuraCyan,
                        unfocusedBorderColor = Color(0x40FFFFFF),
                        focusedContainerColor = Color(0x20000000),
                        unfocusedContainerColor = Color(0x20000000)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Quick Inspiration:",
                    fontSize = 11.sp,
                    color = AuraTextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    sampleTopics.take(3).forEach { sample ->
                        Box(
                            modifier = Modifier
                                .padding(end = 6.dp, bottom = 6.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x30FFFFFF))
                                .clickable { topic = sample }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = sample,
                                fontSize = 11.sp,
                                color = AuraTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Industry & Domain",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraTextPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    industries.forEach { ind ->
                        val isSelected = selectedIndustry == ind
                        Box(
                            modifier = Modifier
                                .padding(end = 6.dp, bottom = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) AuraCyan.copy(alpha = 0.25f) else Color(0x20FFFFFF))
                                .border(
                                    1.dp,
                                    if (isSelected) AuraCyan else Color.Transparent,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedIndustry = ind }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = ind,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AuraCyan else AuraTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Presentation Tone",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraTextPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                FlowRow(modifier = Modifier.fillMaxWidth()) {
                    tones.forEach { t ->
                        val isSelected = selectedTone == t
                        Box(
                            modifier = Modifier
                                .padding(end = 6.dp, bottom = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) Color(0xFFA855F7).copy(alpha = 0.25f) else Color(0x20FFFFFF))
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFFA855F7) else Color.Transparent,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedTone = t }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = t,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFFD8B4FE) else AuraTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Slide Count: ${slideCount.toInt()} slides",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AuraTextPrimary
                    )
                }

                Slider(
                    value = slideCount,
                    onValueChange = { slideCount = it },
                    valueRange = 3f..6f,
                    steps = 2,
                    colors = SliderDefaults.colors(
                        thumbColor = AuraCyan,
                        activeTrackColor = AuraCyan,
                        inactiveTrackColor = Color(0x30FFFFFF)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Glass Theme Aesthetic",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraTextPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    DeckTheme.values().forEach { theme ->
                        val isSelected = selectedTheme == theme
                        val accentColor = parseColorHex(theme.primaryAccentHex, AuraCyan)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 3.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) accentColor.copy(alpha = 0.25f) else Color(0x20FFFFFF))
                                .border(
                                    1.5.dp,
                                    if (isSelected) accentColor else Color(0x20FFFFFF),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedTheme = theme }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(accentColor)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = theme.displayName.split(" ").first(),
                                    fontSize = 10.sp,
                                    color = if (isSelected) accentColor else AuraTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            GlassButton(
                text = "Generate Deck",
                icon = Icons.Default.AutoAwesome,
                isPrimary = true,
                onClick = {
                    val prompt = topic.ifBlank { "Next-Gen Enterprise Platform" }
                    onGenerate(prompt, selectedIndustry, selectedTone, slideCount.toInt(), selectedTheme)
                }
            )
        },
        dismissButton = {
            GlassButton(
                text = "Cancel",
                icon = Icons.Default.Close,
                onClick = onDismiss
            )
        }
    )
}
