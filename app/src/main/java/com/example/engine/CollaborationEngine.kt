package com.example.engine

import com.example.model.Collaborator
import com.example.model.DeckComment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class CollaborationEngine(
    private val scope: CoroutineScope
) {
    private val _collaborators = MutableStateFlow<List<Collaborator>>(getDefaultCollaborators())
    val collaborators: StateFlow<List<Collaborator>> = _collaborators.asStateFlow()

    private val _liveActivity = MutableSharedFlow<String>(replay = 1)
    val liveActivity: SharedFlow<String> = _liveActivity.asSharedFlow()

    private val _isLiveSimulationEnabled = MutableStateFlow(true)
    val isLiveSimulationEnabled: StateFlow<Boolean> = _isLiveSimulationEnabled.asStateFlow()

    private var simulationJob: Job? = null

    init {
        startSimulation()
    }

    private fun getDefaultCollaborators(): List<Collaborator> {
        return listOf(
            Collaborator(
                id = "collab_1",
                name = "Elena Rostova",
                role = "Lead Designer",
                colorHex = "#00F0FF",
                initials = "ER",
                cursorXPercent = 0.35f,
                cursorYPercent = 0.42f,
                statusMessage = "Refining glass specular shader"
            ),
            Collaborator(
                id = "collab_2",
                name = "Marcus Vance",
                role = "VP Product",
                colorHex = "#A855F7",
                initials = "MV",
                cursorXPercent = 0.65f,
                cursorYPercent = 0.60f,
                statusMessage = "Reviewing Q3 ARR figures"
            ),
            Collaborator(
                id = "collab_3",
                name = "Kenji Sato",
                role = "Founding Partner",
                colorHex = "#10B981",
                initials = "KS",
                cursorXPercent = 0.78f,
                cursorYPercent = 0.28f,
                statusMessage = "Adding presenter notes"
            )
        )
    }

    fun toggleLiveSimulation(enabled: Boolean) {
        _isLiveSimulationEnabled.value = enabled
        if (enabled) {
            startSimulation()
        } else {
            simulationJob?.cancel()
        }
    }

    private fun startSimulation() {
        simulationJob?.cancel()
        simulationJob = scope.launch(Dispatchers.Default) {
            val activities = listOf(
                "Elena moved the Keynote Hero heading",
                "Marcus highlighted the +310% ARR metric card",
                "Kenji verified speaker transition notes",
                "Elena adjusted Bento card specular padding",
                "Marcus added comment: 'Highlight gross margin moat'",
                "AI Layout Engine synced slide layout across 3 nodes"
            )

            var step = 0
            while (isActive) {
                delay(3200)
                if (!_isLiveSimulationEnabled.value) continue

                // Smoothly wander collaborator cursors within 0.10f to 0.90f
                _collaborators.value = _collaborators.value.map { peer ->
                    val deltaX = (Random.nextFloat() - 0.5f) * 0.12f
                    val deltaY = (Random.nextFloat() - 0.5f) * 0.12f
                    val newX = (peer.cursorXPercent + deltaX).coerceIn(0.12f, 0.88f)
                    val newY = (peer.cursorYPercent + deltaY).coerceIn(0.15f, 0.85f)
                    peer.copy(
                        cursorXPercent = newX,
                        cursorYPercent = newY
                    )
                }

                // Broadcast periodic live collaboration activity
                if (step % 2 == 0) {
                    val activity = activities[Random.nextInt(activities.size)]
                    _liveActivity.emit(activity)
                }
                step++
            }
        }
    }

    fun inviteCollaborator(name: String, role: String): Collaborator {
        val colors = listOf("#FF007F", "#3B82F6", "#F59E0B", "#10B981", "#8B5CF6", "#06B6D4")
        val initials = name.trim().split(" ")
            .mapNotNull { it.firstOrNull()?.uppercase() }
            .take(2)
            .joinToString("")
            .ifEmpty { "CO" }

        val newPeer = Collaborator(
            id = UUID.randomUUID().toString(),
            name = name.ifEmpty { "Guest Reviewer" },
            role = role.ifEmpty { "Collaborator" },
            colorHex = colors[Random.nextInt(colors.size)],
            initials = initials,
            cursorXPercent = 0.5f,
            cursorYPercent = 0.5f,
            statusMessage = "Joined session"
        )

        _collaborators.value = _collaborators.value + newPeer
        scope.launch {
            _liveActivity.emit("${newPeer.name} joined the live presentation session")
        }
        return newPeer
    }

    fun postComment(
        slideId: String,
        authorName: String,
        authorRole: String,
        content: String,
        xPercent: Float,
        yPercent: Float
    ): DeckComment {
        val comment = DeckComment(
            slideId = slideId,
            authorName = authorName,
            authorRole = authorRole,
            colorHex = "#00F0FF",
            content = content,
            xPercent = xPercent,
            yPercent = yPercent
        )
        scope.launch {
            _liveActivity.emit("$authorName commented: \"$content\"")
        }
        return comment
    }
}
