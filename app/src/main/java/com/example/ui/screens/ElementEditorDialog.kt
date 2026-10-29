package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ElementType
import com.example.model.SlideElement
import com.example.ui.components.GlassButton
import com.example.ui.theme.AuraCardDark
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary

@Composable
fun ElementEditorDialog(
    element: SlideElement,
    onDismiss: () -> Unit,
    onSave: (content: String, secondaryContent: String) -> Unit,
    onAiPolish: (tone: String) -> Unit
) {
    var contentText by remember(element) { mutableStateOf(element.content) }
    var secondaryText by remember(element) { mutableStateOf(element.secondaryContent) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AuraCardDark,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = AuraCyan
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit ${element.type.name.replace("_", " ")}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuraTextPrimary
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (element.type == ElementType.METRIC_CARD) "Stat / Metric Value" else "Primary Text",
                    fontSize = 12.sp,
                    color = AuraTextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = contentText,
                    onValueChange = { contentText = it },
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

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (element.type == ElementType.METRIC_CARD) "Label / Metric Subtitle" else "Secondary Text / Bullets",
                    fontSize = 12.sp,
                    color = AuraTextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = secondaryText,
                    onValueChange = { secondaryText = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
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

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "AI Quick Polish:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AuraCyan
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row {
                    GlassButton(
                        text = "Punchy",
                        onClick = {
                            val polished = com.example.engine.AiLayoutEngine.polishCopy(contentText, "punchy")
                            contentText = polished
                        },
                        accentColor = AuraCyan
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    GlassButton(
                        text = "Visionary",
                        onClick = {
                            val polished = com.example.engine.AiLayoutEngine.polishCopy(contentText, "visionary")
                            contentText = polished
                        },
                        accentColor = Color(0xFFA855F7)
                    )
                }
            }
        },
        confirmButton = {
            GlassButton(
                text = "Apply Changes",
                icon = Icons.Default.Check,
                isPrimary = true,
                onClick = {
                    onSave(contentText, secondaryText)
                    onDismiss()
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
