package com.example.data

import com.example.engine.AiLayoutEngine
import com.example.model.DeckComment
import com.example.model.DeckTheme
import com.example.model.ElementType
import com.example.model.LayoutPreset
import com.example.model.Slide
import com.example.model.SlideDeck
import com.example.model.SlideElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class DeckRepository {

    private val _decks = MutableStateFlow<List<SlideDeck>>(emptyList())
    val decks: StateFlow<List<SlideDeck>> = _decks.asStateFlow()

    // Undo & Redo stacks for the currently active deck
    private val undoStack = mutableListOf<SlideDeck>()
    private val redoStack = mutableListOf<SlideDeck>()

    init {
        seedInitialDecks()
    }

    private fun seedInitialDecks() {
        val deck1 = AiLayoutEngine.generateDeck(
            topic = "AuraIntelligence • Series A Keynote",
            industry = "DeepTech & AI Infrastructure",
            tone = "Visionary",
            slideCount = 5,
            theme = DeckTheme.AURORA_DARK
        ).copy(
            comments = listOf(
                DeckComment(
                    slideId = "slide_demo_1",
                    authorName = "Elena Rostova",
                    authorRole = "Lead Designer",
                    colorHex = "#00F0FF",
                    content = "The contrast on the ARR metric card looks perfect with frosted glass!",
                    xPercent = 0.45f,
                    yPercent = 0.52f
                )
            )
        )

        val deck2 = AiLayoutEngine.generateDeck(
            topic = "2026 Global Product Architecture",
            industry = "Cloud Distributed Systems",
            tone = "Executive",
            slideCount = 4,
            theme = DeckTheme.FROSTED_OBSIDIAN
        )

        val deck3 = AiLayoutEngine.generateDeck(
            topic = "Venture Growth & Unit Economics",
            industry = "Fintech & Capital Markets",
            tone = "Data-Driven",
            slideCount = 4,
            theme = DeckTheme.EMERALD_VENTURE
        )

        _decks.value = listOf(deck1, deck2, deck3)
    }

    fun getDeck(deckId: String): SlideDeck? {
        return _decks.value.firstOrNull { it.id == deckId }
    }

    fun saveDeck(updatedDeck: SlideDeck, recordHistory: Boolean = true) {
        val current = _decks.value.firstOrNull { it.id == updatedDeck.id }
        if (recordHistory && current != null) {
            undoStack.add(current)
            redoStack.clear()
            if (undoStack.size > 25) undoStack.removeAt(0)
        }

        _decks.value = _decks.value.map {
            if (it.id == updatedDeck.id) updatedDeck.copy(updatedAt = System.currentTimeMillis()) else it
        }
    }

    fun createNewDeck(
        topic: String,
        industry: String,
        tone: String,
        slideCount: Int,
        theme: DeckTheme
    ): SlideDeck {
        val newDeck = AiLayoutEngine.generateDeck(topic, industry, tone, slideCount, theme)
        _decks.value = listOf(newDeck) + _decks.value
        return newDeck
    }

    fun duplicateDeck(deckId: String): SlideDeck? {
        val original = getDeck(deckId) ?: return null
        val cloned = original.copy(
            id = UUID.randomUUID().toString(),
            title = "${original.title} (Copy)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        _decks.value = listOf(cloned) + _decks.value
        return cloned
    }

    fun deleteDeck(deckId: String) {
        _decks.value = _decks.value.filter { it.id != deckId }
    }

    fun canUndo(): Boolean = undoStack.isNotEmpty()
    fun canRedo(): Boolean = redoStack.isNotEmpty()

    fun undo(currentDeck: SlideDeck): SlideDeck? {
        if (undoStack.isEmpty()) return null
        val previous = undoStack.removeAt(undoStack.lastIndex)
        redoStack.add(currentDeck)
        saveDeck(previous, recordHistory = false)
        return previous
    }

    fun redo(currentDeck: SlideDeck): SlideDeck? {
        if (redoStack.isEmpty()) return null
        val next = redoStack.removeAt(redoStack.lastIndex)
        undoStack.add(currentDeck)
        saveDeck(next, recordHistory = false)
        return next
    }

    fun addSlide(deckId: String, category: String = "Content"): SlideDeck? {
        val deck = getDeck(deckId) ?: return null
        val slideNumber = deck.slides.size + 1
        val newSlide = Slide(
            id = UUID.randomUUID().toString(),
            title = "Section $slideNumber",
            category = category,
            layoutPreset = LayoutPreset.BENTO_GRID,
            elements = listOf(
                SlideElement(
                    type = ElementType.BADGE,
                    content = "NEW SLIDE",
                    xPercent = 0.08f,
                    yPercent = 0.10f,
                    widthPercent = 0.30f,
                    heightPercent = 0.07f,
                    accentColorHex = "#00F0FF"
                ),
                SlideElement(
                    type = ElementType.HEADING,
                    content = "Keynote Objective $slideNumber",
                    secondaryContent = "Outline core achievements and strategic deliverables",
                    xPercent = 0.08f,
                    yPercent = 0.18f,
                    widthPercent = 0.84f,
                    heightPercent = 0.14f,
                    fontSizeSp = 22f
                ),
                SlideElement(
                    type = ElementType.GLASS_CONTAINER,
                    content = "Strategic Milestone",
                    secondaryContent = "Drag elements freely or tap AI Magic Re-Layout to balance composition automatically.",
                    xPercent = 0.08f,
                    yPercent = 0.36f,
                    widthPercent = 0.84f,
                    heightPercent = 0.52f,
                    fontSizeSp = 14f
                )
            ),
            presenterNotes = "Outline the key takeaway of this slide in 30 seconds."
        )

        val updatedDeck = deck.copy(slides = deck.slides + newSlide)
        saveDeck(updatedDeck)
        return updatedDeck
    }

    fun updateElementPosition(
        deckId: String,
        slideId: String,
        elementId: String,
        xPercent: Float,
        yPercent: Float
    ): SlideDeck? {
        val deck = getDeck(deckId) ?: return null
        val updatedSlides = deck.slides.map { slide ->
            if (slide.id == slideId) {
                val updatedElements = slide.elements.map { el ->
                    if (el.id == elementId) {
                        el.copy(
                            xPercent = xPercent.coerceIn(0.01f, 0.95f - el.widthPercent),
                            yPercent = yPercent.coerceIn(0.01f, 0.95f - el.heightPercent)
                        )
                    } else el
                }
                slide.copy(elements = updatedElements)
            } else slide
        }
        val updated = deck.copy(slides = updatedSlides)
        saveDeck(updated, recordHistory = false)
        return updated
    }

    fun addElementToSlide(
        deckId: String,
        slideId: String,
        type: ElementType
    ): SlideDeck? {
        val deck = getDeck(deckId) ?: return null
        val newEl = when (type) {
            ElementType.HEADING -> SlideElement(
                type = ElementType.HEADING,
                content = "Impact Statement",
                xPercent = 0.10f,
                yPercent = 0.20f,
                widthPercent = 0.75f,
                heightPercent = 0.12f,
                fontSizeSp = 22f
            )
            ElementType.METRIC_CARD -> SlideElement(
                type = ElementType.METRIC_CARD,
                content = "+240%",
                secondaryContent = "Annualized Throughput",
                trendValue = "Tier 1",
                xPercent = 0.30f,
                yPercent = 0.45f,
                widthPercent = 0.35f,
                heightPercent = 0.35f,
                accentColorHex = "#00F0FF"
            )
            ElementType.BADGE -> SlideElement(
                type = ElementType.BADGE,
                content = "AI ACCELERATED",
                xPercent = 0.10f,
                yPercent = 0.10f,
                widthPercent = 0.35f,
                heightPercent = 0.07f,
                accentColorHex = "#10B981"
            )
            ElementType.STICKY_NOTE -> SlideElement(
                type = ElementType.STICKY_NOTE,
                content = "Review by Sarah: Re-verify Q4 numbers before presentation.",
                xPercent = 0.60f,
                yPercent = 0.15f,
                widthPercent = 0.32f,
                heightPercent = 0.24f,
                accentColorHex = "#F59E0B"
            )
            ElementType.QUOTE -> SlideElement(
                type = ElementType.QUOTE,
                content = "\"Simplicity is prerequisite for reliability.\"",
                secondaryContent = "— Edsger Dijkstra",
                xPercent = 0.10f,
                yPercent = 0.50f,
                widthPercent = 0.80f,
                heightPercent = 0.22f,
                accentColorHex = "#8B5CF6"
            )
            else -> SlideElement(
                type = ElementType.GLASS_CONTAINER,
                content = "Key Feature Highlight",
                secondaryContent = "Seamless real-time updates and hardware-accelerated canvas.",
                xPercent = 0.20f,
                yPercent = 0.40f,
                widthPercent = 0.60f,
                heightPercent = 0.35f
            )
        }

        val updatedSlides = deck.slides.map { slide ->
            if (slide.id == slideId) {
                slide.copy(elements = slide.elements + newEl)
            } else slide
        }
        val updated = deck.copy(slides = updatedSlides)
        saveDeck(updated)
        return updated
    }
}
