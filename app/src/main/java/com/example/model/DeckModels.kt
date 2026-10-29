package com.example.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class DeckTheme(
    val displayName: String,
    val primaryAccentHex: String,
    val secondaryAccentHex: String,
    val backgroundStartHex: String,
    val backgroundEndHex: String,
    val cardBackgroundHex: String,
    val textColorHex: String,
    val subtitleColorHex: String
) {
    AURORA_DARK(
        displayName = "Aurora Dark",
        primaryAccentHex = "#00F0FF",
        secondaryAccentHex = "#8B5CF6",
        backgroundStartHex = "#070B14",
        backgroundEndHex = "#0E172A",
        cardBackgroundHex = "#20162544",
        textColorHex = "#F8FAFC",
        subtitleColorHex = "#94A3B8"
    ),
    FROSTED_OBSIDIAN(
        displayName = "Frosted Obsidian",
        primaryAccentHex = "#38BDF8",
        secondaryAccentHex = "#818CF8",
        backgroundStartHex = "#0B0F19",
        backgroundEndHex = "#1E293B",
        cardBackgroundHex = "#28182234",
        textColorHex = "#FFFFFF",
        subtitleColorHex = "#CBD5E1"
    ),
    CYBER_SYNTH(
        displayName = "Cyber Synth",
        primaryAccentHex = "#EC4899",
        secondaryAccentHex = "#06B6D4",
        backgroundStartHex = "#0D0B18",
        backgroundEndHex = "#1A102F",
        cardBackgroundHex = "#282A1B4E",
        textColorHex = "#FFFFFF",
        subtitleColorHex = "#E2E8F0"
    ),
    EMERALD_VENTURE(
        displayName = "Emerald Venture",
        primaryAccentHex = "#10B981",
        secondaryAccentHex = "#06D6A0",
        backgroundStartHex = "#061311",
        backgroundEndHex = "#0B2922",
        cardBackgroundHex = "#220D332A",
        textColorHex = "#F0FDF4",
        subtitleColorHex = "#A7F3D0"
    ),
    ROYAL_CHAMPAGNE(
        displayName = "Royal Champagne",
        primaryAccentHex = "#F59E0B",
        secondaryAccentHex = "#FB923C",
        backgroundStartHex = "#140F07",
        backgroundEndHex = "#271C0C",
        cardBackgroundHex = "#263B2610",
        textColorHex = "#FFFBEB",
        subtitleColorHex = "#FDE68A"
    )
}

enum class ElementType {
    HEADING,
    SUBTITLE,
    BODY,
    METRIC_CARD,
    GLASS_CONTAINER,
    BADGE,
    QUOTE,
    TIMELINE_STEP,
    STICKY_NOTE
}

enum class LayoutPreset(val title: String, val iconName: String, val description: String) {
    BENTO_GRID("Bento Grid", "GridView", "Apple-style modular glass cards with focal metrics"),
    ASYMMETRIC_SPLIT("Asymmetric Split", "ViewSidebar", "Bold hero statement with paired glass details"),
    EXECUTIVE_METRICS("Executive Stats", "BarChart", "High-impact metric capsules with status trends"),
    MINIMAL_CENTER("Minimal Center", "CenterFocusStrong", "Ultra-clean high-contrast centerpiece layout"),
    TIMELINE_FLOW("Timeline Roadmap", "Timeline", "Sequential milestones with glowing connector trails"),
    PROBLEM_SOLUTION("Problem / Solution", "Compare", "Dual comparative glass columns with badges")
}

data class SlideElement(
    val id: String = UUID.randomUUID().toString(),
    val type: ElementType,
    val content: String,
    val secondaryContent: String = "",
    val badgeLabel: String = "",
    val trendValue: String = "",
    val xPercent: Float, // 0.0 to 1.0 (relative to canvas width)
    val yPercent: Float, // 0.0 to 1.0 (relative to canvas height)
    val widthPercent: Float, // width fraction of canvas
    val heightPercent: Float, // height fraction of canvas
    val fontSizeSp: Float = 16f,
    val accentColorHex: String? = null,
    val zIndex: Int = 0,
    val isGlassMorphic: Boolean = true
)

data class Slide(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String = "Slide",
    val elements: List<SlideElement> = emptyList(),
    val layoutPreset: LayoutPreset = LayoutPreset.BENTO_GRID,
    val presenterNotes: String = "",
    val aiLayoutRationale: String = "AI optimized visual weight balance with clear focal points.",
    val backgroundGlowColor: String? = null
)

data class Collaborator(
    val id: String,
    val name: String,
    val role: String,
    val colorHex: String,
    val initials: String,
    val cursorXPercent: Float = 0.5f,
    val cursorYPercent: Float = 0.5f,
    val activeSlideId: String = "",
    val isOnline: Boolean = true,
    val statusMessage: String = "Viewing"
)

data class DeckComment(
    val id: String = UUID.randomUUID().toString(),
    val slideId: String,
    val authorName: String,
    val authorRole: String,
    val colorHex: String,
    val content: String,
    val xPercent: Float,
    val yPercent: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)

data class SlideDeck(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String = "",
    val theme: DeckTheme = DeckTheme.AURORA_DARK,
    val aspectRatio: String = "16:9", // 16:9 or 4:3
    val slides: List<Slide> = emptyList(),
    val collaborators: List<Collaborator> = emptyList(),
    val comments: List<DeckComment> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
