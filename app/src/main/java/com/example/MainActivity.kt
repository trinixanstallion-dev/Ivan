package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.model.DeckTheme
import com.example.ui.screens.CanvasEditorScreen
import com.example.ui.screens.CollaborationDialog
import com.example.ui.screens.DeckListScreen
import com.example.ui.screens.PresentationModeScreen
import com.example.ui.theme.AuraDeckTheme
import com.example.viewmodel.DeckViewModel

enum class AppScreen {
    DECK_LIST,
    CANVAS_EDITOR
}

class MainActivity : ComponentActivity() {

    private val viewModel: DeckViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentDeck by viewModel.currentDeck.collectAsState()
            val isPresentationMode by viewModel.isPresentationMode.collectAsState()
            val isCollaborationDialogOpen by viewModel.isCollaborationDialogOpen.collectAsState()
            val collaborators by viewModel.collaborators.collectAsState()
            val currentSlideIndex by viewModel.currentSlideIndex.collectAsState()

            var currentScreen by remember { mutableStateOf(AppScreen.DECK_LIST) }

            val theme = currentDeck?.theme ?: DeckTheme.AURORA_DARK

            AuraDeckTheme(deckTheme = theme) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF070B14))
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    when {
                        // FULLSCREEN PRESENTATION MODE
                        isPresentationMode && currentDeck != null -> {
                            PresentationModeScreen(
                                deck = currentDeck!!,
                                currentSlideIndex = currentSlideIndex,
                                onSelectSlide = { idx -> viewModel.selectSlide(idx) },
                                onExit = { viewModel.togglePresentationMode() }
                            )
                        }

                        // CANVAS EDITOR SCREEN
                        currentScreen == AppScreen.CANVAS_EDITOR && currentDeck != null -> {
                            CanvasEditorScreen(
                                viewModel = viewModel,
                                onBackToDecks = {
                                    currentScreen = AppScreen.DECK_LIST
                                }
                            )
                        }

                        // DECK LIST SCREEN (STUDIO HUB)
                        else -> {
                            DeckListScreen(
                                viewModel = viewModel,
                                onOpenDeck = { deckId ->
                                    viewModel.selectDeck(deckId)
                                    currentScreen = AppScreen.CANVAS_EDITOR
                                }
                            )
                        }
                    }

                    // COLLABORATION DIALOG
                    if (isCollaborationDialogOpen) {
                        CollaborationDialog(
                            collaborators = collaborators,
                            isSimulationEnabled = true,
                            onToggleSimulation = { /* handled in engine */ },
                            onInvite = { name, role ->
                                viewModel.inviteCollaborator(name, role)
                            },
                            onDismiss = { viewModel.openCollaborationDialog(false) }
                        )
                    }
                }
            }
        }
    }
}
