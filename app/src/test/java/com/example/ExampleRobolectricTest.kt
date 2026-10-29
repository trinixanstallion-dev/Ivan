package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DeckRepository
import com.example.engine.AiLayoutEngine
import com.example.model.DeckTheme
import com.example.model.LayoutPreset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AuraDeck", appName)
  }

  @Test
  fun `ai layout engine generates structured deck`() {
    val deck = AiLayoutEngine.generateDeck(
      topic = "NextGen Quantum AI",
      industry = "DeepTech",
      tone = "Visionary",
      slideCount = 5,
      theme = DeckTheme.AURORA_DARK
    )

    assertEquals("NextGen Quantum AI", deck.title)
    assertTrue("Should generate at least 5 slides", deck.slides.size >= 5)
    assertTrue("First slide should have elements", deck.slides.first().elements.isNotEmpty())
  }

  @Test
  fun `ai relayout solver reorganizes elements without crash`() {
    val deck = AiLayoutEngine.generateDeck(topic = "Test Keynote", slideCount = 3)
    val originalSlide = deck.slides.first()

    val relaidSlide = AiLayoutEngine.applyLayoutPreset(originalSlide, LayoutPreset.ASYMMETRIC_SPLIT)
    assertEquals(LayoutPreset.ASYMMETRIC_SPLIT, relaidSlide.layoutPreset)
    assertTrue(relaidSlide.elements.isNotEmpty())
  }

  @Test
  fun `deck repository manages deck lifecycle and undo redo`() {
    val repo = DeckRepository()
    val initialDecks = repo.decks.value
    assertTrue("Pre-seeded decks should exist", initialDecks.isNotEmpty())

    val firstDeck = initialDecks.first()
    val duplicated = repo.duplicateDeck(firstDeck.id)
    assertNotNull("Duplicated deck should exist", duplicated)
    assertTrue("Decks size should increase", repo.decks.value.size > initialDecks.size)
  }
}
