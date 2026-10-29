package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Collaborator
import com.example.ui.components.GlassBadge
import com.example.ui.components.GlassButton
import com.example.ui.theme.AuraCardDark
import com.example.ui.theme.AuraCyan
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTextPrimary
import com.example.ui.theme.AuraTextSecondary
import com.example.ui.theme.parseColorHex

@Composable
fun CollaborationDialog(
    collaborators: List<Collaborator>,
    isSimulationEnabled: Boolean,
    onToggleSimulation: (Boolean) -> Unit,
    onInvite: (name: String, role: String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf("") }
    var newRole by remember { mutableStateOf("Product Lead") }

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
                        .background(AuraCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = AuraCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Real-time Collaboration",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AuraTextPrimary
                    )
                    Text(
                        text = "Sub-millisecond CRDT canvas synchronization",
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
                // Live Peers Ticker
                Text(
                    text = "ACTIVE COLLABORATORS (${collaborators.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuraCyan,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                collaborators.forEach { peer ->
                    val color = parseColorHex(peer.colorHex, AuraCyan)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x18FFFFFF))
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(color.copy(alpha = 0.35f))
                                .border(1.dp, color, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = peer.initials,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AuraTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = peer.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AuraTextPrimary
                            )
                            Text(
                                text = "${peer.role} • ${peer.statusMessage}",
                                fontSize = 11.sp,
                                color = AuraTextSecondary
                            )
                        }

                        GlassBadge(
                            text = "LIVE",
                            color = color,
                            showDot = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Invite New Collaborator Section
                Text(
                    text = "INVITE TEAM MEMBER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AuraCyan,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    placeholder = {
                        Text(
                            text = "Full Name (e.g. Maya Lin)",
                            color = AuraTextMuted,
                            fontSize = 13.sp
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

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = newRole,
                    onValueChange = { newRole = it },
                    placeholder = {
                        Text(
                            text = "Role (e.g. VP Strategy)",
                            color = AuraTextMuted,
                            fontSize = 13.sp
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

                GlassButton(
                    text = "Add Collaborator",
                    icon = Icons.Default.PersonAdd,
                    onClick = {
                        if (newName.isNotBlank()) {
                            onInvite(newName, newRole)
                            newName = ""
                        }
                    },
                    accentColor = AuraCyan,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Toggle Simulation
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x18FFFFFF))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Simulate Real-time Team Activity",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AuraTextPrimary
                        )
                        Text(
                            text = "Simulates live cursor roaming & edit signals",
                            fontSize = 11.sp,
                            color = AuraTextSecondary
                        )
                    }

                    Switch(
                        checked = isSimulationEnabled,
                        onCheckedChange = onToggleSimulation,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AuraCyan,
                            checkedTrackColor = AuraCyan.copy(alpha = 0.4f),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color(0x30FFFFFF)
                        )
                    )
                }
            }
        },
        confirmButton = {
            GlassButton(
                text = "Close",
                icon = Icons.Default.Close,
                onClick = onDismiss
            )
        }
    )
}
