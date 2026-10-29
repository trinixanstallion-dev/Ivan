package com.example.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DeckRepository
import com.example.engine.AiLayoutEngine
import com.example.engine.CollaborationEngine
import com.example.model.Collaborator
import com.example.model.DeckTheme
import com.example.model.ElementType
import com.example.model.LayoutPreset
import com.example.model.Slide
import com.example.model.SlideDeck
import com.example.model.SlideElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class DeckViewModel(
    private val repository: DeckRepository = DeckRepository()
) : ViewModel() {

    private val collaborationEngine = CollaborationEngine(viewModelScope)

    val allDecks: StateFlow<List<SlideDeck>> = repository.decks

    private val _currentDeckId = MutableStateFlow<String?>(null)
    val currentDeckId: StateFlow<String?> = _currentDeckId.asStateFlow()

    private val _currentSlideIndex = MutableStateFlow(0)
    val currentSlideIndex: StateFlow<Int> = _currentSlideIndex.asStateFlow()

    private val _selectedElementId = MutableStateFlow<String?>(null)
    val selectedElementId: StateFlow<String?> = _selectedElementId.asStateFlow()

    val collaborators: StateFlow<List<Collaborator>> = collaborationEngine.collaborators
    val liveActivity: SharedFlow<String> = collaborationEngine.liveActivity

    private val _isPresentationMode = MutableStateFlow(false)
    val isPresentationMode: StateFlow<Boolean> = _isPresentationMode.asStateFlow()

    private val _isAiGeneratorDialogOpen = MutableStateFlow(false)
    val isAiGeneratorDialogOpen: StateFlow<Boolean> = _isAiGeneratorDialogOpen.asStateFlow()

    private val _isCollaborationDialogOpen = MutableStateFlow(false)
    val isCollaborationDialogOpen: StateFlow<Boolean> = _isCollaborationDialogOpen.asStateFlow()

    private val _isNotesSheetOpen = MutableStateFlow(false)
    val isNotesSheetOpen: StateFlow<Boolean> = _isNotesSheetOpen.asStateFlow()

    private val _isElementEditorOpen = MutableStateFlow(false)
    val isElementEditorOpen: StateFlow<Boolean> = _isElementEditorOpen.asStateFlow()

    private val _showPeerCursors = MutableStateFlow(true)
    val showPeerCursors: StateFlow<Boolean> = _showPeerCursors.asStateFlow()

    private val _isLaserPointerActive = MutableStateFlow(false)
    val isLaserPointerActive: StateFlow<Boolean> = _isLaserPointerActive.asStateFlow()

    private val _laserPosition = MutableStateFlow<Offset?>(null)
    val laserPosition: StateFlow<Offset?> = _laserPosition.asStateFlow()

    private val _showHorizontalGuide = MutableStateFlow(false)
    val showHorizontalGuide: StateFlow<Boolean> = _showHorizontalGuide.asStateFlow()

    private val _showVerticalGuide = MutableStateFlow(false)
    val showVerticalGuide: StateFlow<Boolean> = _showVerticalGuide.asStateFlow()

    init {
        // Select the first deck by default
        viewModelScope.launch {
            allDecks.collect { decks ->
                if (_currentDeckId.value == null && decks.isNotEmpty()) {
                    _currentDeckId.value = decks.first().id
                }
            }
        }
    }

    val currentDeck: StateFlow<SlideDeck?> = kotlinx.coroutines.flow.combine(
        allDecks,
        _currentDeckId
    ) { decks, id ->
        decks.firstOrNull { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val currentSlide: StateFlow<Slide?> = kotlinx.coroutines.flow.combine(
        currentDeck,
        _currentSlideIndex
    ) { deck, index ->
        deck?.slides?.getOrNull(index)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun selectDeck(deckId: String?) {
        _currentDeckId.value = deckId
        _currentSlideIndex.value = 0
        _selectedElementId.value = null
    }

    fun selectSlide(index: Int) {
        val deck = currentDeck.value ?: return
        if (index in deck.slides.indices) {
            _currentSlideIndex.value = index
            _selectedElementId.value = null
        }
    }

    fun nextSlide() {
        val deck = currentDeck.value ?: return
        if (_currentSlideIndex.value < deck.slides.lastIndex) {
            _currentSlideIndex.value += 1
            _selectedElementId.value = null
        }
    }

    fun prevSlide() {
        if (_currentSlideIndex.value > 0) {
            _currentSlideIndex.value -= 1
            _selectedElementId.value = null
        }
    }

    fun selectElement(elementId: String?) {
        _selectedElementId.value = elementId
    }

    fun updateElementPosition(elementId: String, newX: Float, newY: Float) {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return

        // Snapping calculation: snap if near center (0.5) or margins
        val snappedX = if (kotlin.math.abs(newX - 0.5f) < 0.025f) {
            _showVerticalGuide.value = true
            0.5f
        } else {
            _showVerticalGuide.value = false
            newX
        }

        val snappedY = if (kotlin.math.abs(newY - 0.5f) < 0.025f) {
            _showHorizontalGuide.value = true
            0.5f
        } else {
            _showHorizontalGuide.value = false
            newY
        }

        val updated = repository.updateElementPosition(deck.id, slide.id, elementId, snappedX, snappedY)
        if (updated != null) {
            // Updated in repository
        }
    }

    fun clearGuides() {
        _showHorizontalGuide.value = false
        _showVerticalGuide.value = false
    }

    fun autoSmartReLayout() {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return

        val relaidSlide = AiLayoutEngine.autoSmartReLayout(slide)
        val updatedSlides = deck.slides.map {
            if (it.id == slide.id) relaidSlide else it
        }
        repository.saveDeck(deck.copy(slides = updatedSlides))
    }

    fun applyLayoutPreset(preset: LayoutPreset) {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return

        val relaidSlide = AiLayoutEngine.applyLayoutPreset(slide, preset)
        val updatedSlides = deck.slides.map {
            if (it.id == slide.id) relaidSlide else it
        }
        repository.saveDeck(deck.copy(slides = updatedSlides))
    }

    fun addElement(type: ElementType) {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return
        val updated = repository.addElementToSlide(deck.id, slide.id, type)
        if (updated != null) {
            // Select the newly added element
            val newSlide = updated.slides.firstOrNull { it.id == slide.id }
            _selectedElementId.value = newSlide?.elements?.lastOrNull()?.id
        }
    }

    fun duplicateSelectedElement() {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return
        val selectedId = _selectedElementId.value ?: return
        val element = slide.elements.firstOrNull { it.id == selectedId } ?: return

        val duplicated = element.copy(
            id = UUID.randomUUID().toString(),
            xPercent = (element.xPercent + 0.05f).coerceAtMost(0.70f),
            yPercent = (element.yPercent + 0.05f).coerceAtMost(0.70f)
        )

        val updatedSlides = deck.slides.map {
            if (it.id == slide.id) it.copy(elements = it.elements + duplicated) else it
        }
        repository.saveDeck(deck.copy(slides = updatedSlides))
        _selectedElementId.value = duplicated.id
    }

    fun deleteSelectedElement() {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return
        val selectedId = _selectedElementId.value ?: return

        val updatedSlides = deck.slides.map {
            if (it.id == slide.id) it.copy(elements = it.elements.filter { el -> el.id != selectedId }) else it
        }
        repository.saveDeck(deck.copy(slides = updatedSlides))
        _selectedElementId.value = null
    }

    fun updateElementContent(elementId: String, content: String, secondaryContent: String = "") {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return

        val updatedSlides = deck.slides.map { s ->
            if (s.id == slide.id) {
                s.copy(elements = s.elements.map { el ->
                    if (el.id == elementId) el.copy(content = content, secondaryContent = secondaryContent) else el
                })
            } else s
        }
        repository.saveDeck(deck.copy(slides = updatedSlides))
    }

    fun polishSelectedElementText(tone: String = "punchy") {
        val deck = currentDeck.value ?: return
        val slide = currentSlide.value ?: return
        val selectedId = _selectedElementId.value ?: return
        val element = slide.elements.firstOrNull { it.id == selectedId } ?: return

        val polished = AiLayoutEngine.polishCopy(element.content, tone)
        updateElementContent(element.id, polished, element.secondaryContent)
    }

    fun setDeckTheme(theme: DeckTheme) {
        val deck = currentDeck.value ?: return
        repository.saveDeck(deck.copy(theme = theme))
    }

    fun addSlide() {
        val deck = currentDeck.value ?: return
        val updated = repository.addSlide(deck.id)
        if (updated != null) {
            _currentSlideIndex.value = updated.slides.lastIndex
            _selectedElementId.value = null
        }
    }

    fun deleteSlide(index: Int) {
        val deck = currentDeck.value ?: return
        if (deck.slides.size <= 1) return

        val updatedSlides = deck.slides.toMutableList().apply { removeAt(index) }
        repository.saveDeck(deck.copy(slides = updatedSlides))
        _currentSlideIndex.value = _currentSlideIndex.value.coerceIn(0, updatedSlides.lastIndex)
        _selectedElementId.value = null
    }

    fun generateNewDeck(
        topic: String,
        industry: String,
        tone: String,
        slideCount: Int,
        theme: DeckTheme
    ) {
        val newDeck = repository.createNewDeck(topic, industry, tone, slideCount, theme)
        _currentDeckId.value = newDeck.id
        _currentSlideIndex.value = 0
        _selectedElementId.value = null
        _isAiGeneratorDialogOpen.value = false
    }

    fun duplicateDeck(deckId: String) {
        val cloned = repository.duplicateDeck(deckId)
        if (cloned != null) {
            _currentDeckId.value = cloned.id
            _currentSlideIndex.value = 0
        }
    }

    fun deleteDeck(deckId: String) {
        repository.deleteDeck(deckId)
        if (_currentDeckId.value == deckId) {
            _currentDeckId.value = allDecks.value.firstOrNull { it.id != deckId }?.id
            _currentSlideIndex.value = 0
        }
    }

    fun duplicateCurrentDeck() {
        val deck = currentDeck.value ?: return
        duplicateDeck(deck.id)
    }

    fun deleteCurrentDeck() {
        val deck = currentDeck.value ?: return
        deleteDeck(deck.id)
    }

    fun undo() {
        val deck = currentDeck.value ?: return
        val restored = repository.undo(deck)
        if (restored != null) {
            _selectedElementId.value = null
        }
    }

    fun redo() {
        val deck = currentDeck.value ?: return
        val restored = repository.redo(deck)
        if (restored != null) {
            _selectedElementId.value = null
        }
    }

    fun canUndo(): Boolean = repository.canUndo()
    fun canRedo(): Boolean = repository.canRedo()

    fun inviteCollaborator(name: String, role: String) {
        collaborationEngine.inviteCollaborator(name, role)
    }

    fun togglePeerCursors() {
        _showPeerCursors.value = !_showPeerCursors.value
    }

    fun togglePresentationMode() {
        _isPresentationMode.value = !_isPresentationMode.value
        _selectedElementId.value = null
        _isLaserPointerActive.value = false
        _laserPosition.value = null
    }

    fun toggleLaserPointer() {
        _isLaserPointerActive.value = !_isLaserPointerActive.value
        if (!_isLaserPointerActive.value) {
            _laserPosition.value = null
        }
    }

    fun updateLaserPosition(offset: Offset?) {
        _laserPosition.value = offset
    }

    fun openAiGeneratorDialog(open: Boolean) {
        _isAiGeneratorDialogOpen.value = open
    }

    fun openCollaborationDialog(open: Boolean) {
        _isCollaborationDialogOpen.value = open
    }

    fun openNotesSheet(open: Boolean) {
        _isNotesSheetOpen.value = open
    }

    fun openElementEditor(open: Boolean) {
        _isElementEditorOpen.value = open
    }
}
