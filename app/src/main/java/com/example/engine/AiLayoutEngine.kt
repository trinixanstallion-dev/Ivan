package com.example.engine

import com.example.model.DeckTheme
import com.example.model.ElementType
import com.example.model.LayoutPreset
import com.example.model.Slide
import com.example.model.SlideDeck
import com.example.model.SlideElement
import java.util.UUID

object AiLayoutEngine {

    fun generateDeck(
        topic: String,
        industry: String = "Artificial Intelligence",
        tone: String = "Visionary",
        slideCount: Int = 5,
        theme: DeckTheme = DeckTheme.AURORA_DARK
    ): SlideDeck {
        val cleanTopic = topic.trim().ifEmpty { "Next-Gen Enterprise Platform" }
        val deckId = UUID.randomUUID().toString()

        val slides = mutableListOf<Slide>()

        // Slide 1: Hero Keynote
        slides.add(
            createTitleSlide(cleanTopic, industry, tone)
        )

        // Slide 2: Problem / Opportunity
        if (slideCount >= 2) {
            slides.add(
                createProblemSolutionSlide(cleanTopic, industry)
            )
        }

        // Slide 3: Executive Metrics / Traction
        if (slideCount >= 3) {
            slides.add(
                createMetricsSlide(cleanTopic, industry)
            )
        }

        // Slide 4: Bento Architecture / Core Platform
        if (slideCount >= 4) {
            slides.add(
                createBentoArchitectureSlide(cleanTopic, industry)
            )
        }

        // Slide 5: Roadmap & Milestones
        if (slideCount >= 5) {
            slides.add(
                createRoadmapSlide(cleanTopic)
            )
        }

        // Slide 6: Vision / Investment Ask (if slideCount > 5)
        if (slideCount >= 6) {
            slides.add(
                createClosingSlide(cleanTopic, industry)
            )
        }

        return SlideDeck(
            id = deckId,
            title = cleanTopic,
            subtitle = "$industry • $tone Presentation",
            theme = theme,
            aspectRatio = "16:9",
            slides = slides,
            collaborators = emptyList()
        )
    }

    private fun createTitleSlide(topic: String, industry: String, tone: String): Slide {
        val elements = listOf(
            SlideElement(
                type = ElementType.BADGE,
                content = "$industry • $tone".uppercase(),
                xPercent = 0.08f,
                yPercent = 0.16f,
                widthPercent = 0.35f,
                heightPercent = 0.08f,
                accentColorHex = "#00F0FF"
            ),
            SlideElement(
                type = ElementType.HEADING,
                content = topic,
                secondaryContent = "The next leap in autonomous intelligence & scale",
                xPercent = 0.08f,
                yPercent = 0.28f,
                widthPercent = 0.84f,
                heightPercent = 0.28f,
                fontSizeSp = 28f
            ),
            SlideElement(
                type = ElementType.GLASS_CONTAINER,
                content = "KEYNOTE OVERVIEW",
                secondaryContent = "An executive synthesis of technology breakthrough, addressable market dynamics, and operational trajectory.",
                xPercent = 0.08f,
                yPercent = 0.60f,
                widthPercent = 0.84f,
                heightPercent = 0.26f,
                fontSizeSp = 14f
            )
        )

        return Slide(
            title = "Introduction",
            category = "Keynote",
            layoutPreset = LayoutPreset.MINIMAL_CENTER,
            elements = elements,
            presenterNotes = "Open with high energy. Emphasize why $topic is a paradigm shift rather than incremental improvement. Pause for 3 seconds after the title.",
            aiLayoutRationale = "Asymmetric focus anchoring the viewer's eye on the core breakthrough with generous breathing room."
        )
    }

    private fun createProblemSolutionSlide(topic: String, industry: String): Slide {
        val elements = listOf(
            SlideElement(
                type = ElementType.BADGE,
                content = "MARKET FRICTION VS BREAKTHROUGH",
                xPercent = 0.08f,
                yPercent = 0.10f,
                widthPercent = 0.50f,
                heightPercent = 0.07f,
                accentColorHex = "#F43F5E"
            ),
            SlideElement(
                type = ElementType.HEADING,
                content = "Legacy Architecture Has Reached Its Limit",
                xPercent = 0.08f,
                yPercent = 0.18f,
                widthPercent = 0.84f,
                heightPercent = 0.14f,
                fontSizeSp = 22f
            ),
            SlideElement(
                type = ElementType.GLASS_CONTAINER,
                content = "The Bottleneck",
                secondaryContent = "• Fragmented pipelines\n• 40% latency overhead\n• Prohibitive operational costs\n• Inflexible legacy infrastructure",
                badgeLabel = "CURRENT STATE",
                xPercent = 0.08f,
                yPercent = 0.36f,
                widthPercent = 0.40f,
                heightPercent = 0.54f,
                fontSizeSp = 13f,
                accentColorHex = "#EF4444"
            ),
            SlideElement(
                type = ElementType.GLASS_CONTAINER,
                content = "The $topic Advantage",
                secondaryContent = "• Zero-latency inference\n• 10x cost reduction\n• Autonomous self-tuning\n• Native multi-cloud federation",
                badgeLabel = "THE SOLUTION",
                xPercent = 0.52f,
                yPercent = 0.36f,
                widthPercent = 0.40f,
                heightPercent = 0.54f,
                fontSizeSp = 13f,
                accentColorHex = "#10B981"
            )
        )

        return Slide(
            title = "Market Problem & Solution",
            category = "Problem / Solution",
            layoutPreset = LayoutPreset.PROBLEM_SOLUTION,
            elements = elements,
            presenterNotes = "Contrast the pain points on the left card before unveiling the right card. Highlight that existing vendors are constrained by technical debt.",
            aiLayoutRationale = "Dual comparative balance allowing instant scanning of contrast and value proposition."
        )
    }

    private fun createMetricsSlide(topic: String, industry: String): Slide {
        val elements = listOf(
            SlideElement(
                type = ElementType.BADGE,
                content = "VALIDATED PERFORMANCE",
                xPercent = 0.08f,
                yPercent = 0.10f,
                widthPercent = 0.40f,
                heightPercent = 0.07f,
                accentColorHex = "#10B981"
            ),
            SlideElement(
                type = ElementType.HEADING,
                content = "Exponential Growth & Platform Proof",
                xPercent = 0.08f,
                yPercent = 0.18f,
                widthPercent = 0.84f,
                heightPercent = 0.14f,
                fontSizeSp = 22f
            ),
            SlideElement(
                type = ElementType.METRIC_CARD,
                content = "$18.4M",
                secondaryContent = "Contracted ARR",
                trendValue = "+310% YoY",
                xPercent = 0.08f,
                yPercent = 0.36f,
                widthPercent = 0.26f,
                heightPercent = 0.48f,
                accentColorHex = "#00F0FF"
            ),
            SlideElement(
                type = ElementType.METRIC_CARD,
                content = "99.98%",
                secondaryContent = "Accuracy Score",
                trendValue = "SOTA Benchmark",
                xPercent = 0.37f,
                yPercent = 0.36f,
                widthPercent = 0.26f,
                heightPercent = 0.48f,
                accentColorHex = "#8B5CF6"
            ),
            SlideElement(
                type = ElementType.METRIC_CARD,
                content = "4.2x",
                secondaryContent = "Customer Efficiency Gain",
                trendValue = "Net ROI",
                xPercent = 0.66f,
                yPercent = 0.36f,
                widthPercent = 0.26f,
                heightPercent = 0.48f,
                accentColorHex = "#10B981"
            )
        )

        return Slide(
            title = "Traction & Metrics",
            category = "Financials & Proof",
            layoutPreset = LayoutPreset.EXECUTIVE_METRICS,
            elements = elements,
            presenterNotes = "Walk through the ARR surge first, then explain the efficiency multiple. This slide establishes indisputable credibility.",
            aiLayoutRationale = "Three-pillar metric alignment engineered for executive scanability and memorability."
        )
    }

    private fun createBentoArchitectureSlide(topic: String, industry: String): Slide {
        val elements = listOf(
            SlideElement(
                type = ElementType.BADGE,
                content = "CORE ENGINE",
                xPercent = 0.08f,
                yPercent = 0.10f,
                widthPercent = 0.30f,
                heightPercent = 0.07f,
                accentColorHex = "#8B5CF6"
            ),
            SlideElement(
                type = ElementType.HEADING,
                content = "Bento Architecture & Intelligence Stack",
                xPercent = 0.08f,
                yPercent = 0.18f,
                widthPercent = 0.84f,
                heightPercent = 0.14f,
                fontSizeSp = 22f
            ),
            SlideElement(
                type = ElementType.GLASS_CONTAINER,
                content = "Unified Core Engine",
                secondaryContent = "Real-time layout computation with multi-modal neural constraints, streaming layout diffs across collaborating nodes.",
                badgeLabel = "FOUNDATION LAYER",
                xPercent = 0.08f,
                yPercent = 0.35f,
                widthPercent = 0.48f,
                heightPercent = 0.52f,
                accentColorHex = "#00F0FF"
            ),
            SlideElement(
                type = ElementType.GLASS_CONTAINER,
                content = "Low-Latency CRDT",
                secondaryContent = "Conflict-free replicated data types ensuring 0-conflict simultaneous drag operations.",
                badgeLabel = "SYNC",
                xPercent = 0.58f,
                yPercent = 0.35f,
                widthPercent = 0.34f,
                heightPercent = 0.24f,
                accentColorHex = "#A855F7"
            ),
            SlideElement(
                type = ElementType.GLASS_CONTAINER,
                content = "Specular Glass Shader",
                secondaryContent = "Hardware-accelerated dynamic frosted blurs and edge-lit refractive borders.",
                badgeLabel = "UI ENGINE",
                xPercent = 0.58f,
                yPercent = 0.62f,
                widthPercent = 0.34f,
                heightPercent = 0.25f,
                accentColorHex = "#10B981"
            )
        )

        return Slide(
            title = "Architecture Bento",
            category = "Technology",
            layoutPreset = LayoutPreset.BENTO_GRID,
            elements = elements,
            presenterNotes = "Explain that the core engine powers both the visual design constraints and the real-time collaboration layer.",
            aiLayoutRationale = "Asymmetric Bento grid providing structural dominance to the core engine while detailing auxiliary features."
        )
    }

    private fun createRoadmapSlide(topic: String): Slide {
        val elements = listOf(
            SlideElement(
                type = ElementType.BADGE,
                content = "STRATEGIC EXECUTION",
                xPercent = 0.08f,
                yPercent = 0.10f,
                widthPercent = 0.36f,
                heightPercent = 0.07f,
                accentColorHex = "#F59E0B"
            ),
            SlideElement(
                type = ElementType.HEADING,
                content = "Product Roadmap & Global Rollout",
                xPercent = 0.08f,
                yPercent = 0.18f,
                widthPercent = 0.84f,
                heightPercent = 0.14f,
                fontSizeSp = 22f
            ),
            SlideElement(
                type = ElementType.TIMELINE_STEP,
                content = "Q1 2026: Core Engine",
                secondaryContent = "Alpha launch of the autonomous layout solver and canvas drag system.",
                badgeLabel = "PHASE 1",
                xPercent = 0.08f,
                yPercent = 0.36f,
                widthPercent = 0.26f,
                heightPercent = 0.50f,
                accentColorHex = "#00F0FF"
            ),
            SlideElement(
                type = ElementType.TIMELINE_STEP,
                content = "Q2 2026: Realtime Mesh",
                secondaryContent = "Global peer-to-peer sync with sub-5ms collaboration latency.",
                badgeLabel = "PHASE 2",
                xPercent = 0.37f,
                yPercent = 0.36f,
                widthPercent = 0.26f,
                heightPercent = 0.50f,
                accentColorHex = "#8B5CF6"
            ),
            SlideElement(
                type = ElementType.TIMELINE_STEP,
                content = "Q4 2026: Global Enterprise",
                secondaryContent = "Multi-region sovereign cloud deployment and Fortune 500 integrations.",
                badgeLabel = "PHASE 3",
                xPercent = 0.66f,
                yPercent = 0.36f,
                widthPercent = 0.26f,
                heightPercent = 0.50f,
                accentColorHex = "#10B981"
            )
        )

        return Slide(
            title = "Timeline Roadmap",
            category = "Roadmap",
            layoutPreset = LayoutPreset.TIMELINE_FLOW,
            elements = elements,
            presenterNotes = "Reassure stakeholders of clear delivery milestones and team velocity.",
            aiLayoutRationale = "Chronological milestone flow with connected visual rhythm."
        )
    }

    private fun createClosingSlide(topic: String, industry: String): Slide {
        val elements = listOf(
            SlideElement(
                type = ElementType.BADGE,
                content = "NEXT HORIZON",
                xPercent = 0.08f,
                yPercent = 0.16f,
                widthPercent = 0.30f,
                heightPercent = 0.08f,
                accentColorHex = "#00F0FF"
            ),
            SlideElement(
                type = ElementType.HEADING,
                content = "The Future of $topic Starts Now",
                secondaryContent = "Partner with us as we redefine how leaders present ideas to the world.",
                xPercent = 0.08f,
                yPercent = 0.28f,
                widthPercent = 0.84f,
                heightPercent = 0.28f,
                fontSizeSp = 28f
            ),
            SlideElement(
                type = ElementType.QUOTE,
                content = "\"The highest form of communication is visual clarity under extreme conviction.\"",
                secondaryContent = "— Executive Board Synthesis",
                xPercent = 0.08f,
                yPercent = 0.62f,
                widthPercent = 0.84f,
                heightPercent = 0.24f,
                fontSizeSp = 15f,
                accentColorHex = "#8B5CF6"
            )
        )

        return Slide(
            title = "Vision & Closing",
            category = "Vision",
            layoutPreset = LayoutPreset.MINIMAL_CENTER,
            elements = elements,
            presenterNotes = "Close with warmth and conviction. Invite open Q&A.",
            aiLayoutRationale = "Clean minimal quote anchor ensuring high emotional resonance."
        )
    }

    fun applyLayoutPreset(slide: Slide, preset: LayoutPreset): Slide {
        val elements = slide.elements
        if (elements.isEmpty()) return slide.copy(layoutPreset = preset)

        val updatedElements = mutableListOf<SlideElement>()
        val headingEl = elements.firstOrNull { it.type == ElementType.HEADING } ?: elements.first()
        val badgeEl = elements.firstOrNull { it.type == ElementType.BADGE }
        val restElements = elements.filter { it.id != headingEl.id && it.id != badgeEl?.id }

        when (preset) {
            LayoutPreset.BENTO_GRID -> {
                badgeEl?.let {
                    updatedElements.add(it.copy(xPercent = 0.08f, yPercent = 0.08f, widthPercent = 0.35f, heightPercent = 0.07f))
                }
                updatedElements.add(
                    headingEl.copy(
                        xPercent = 0.08f,
                        yPercent = if (badgeEl != null) 0.16f else 0.08f,
                        widthPercent = 0.84f,
                        heightPercent = 0.14f,
                        fontSizeSp = 22f
                    )
                )

                if (restElements.size == 1) {
                    updatedElements.add(restElements[0].copy(xPercent = 0.08f, yPercent = 0.34f, widthPercent = 0.84f, heightPercent = 0.55f))
                } else if (restElements.size == 2) {
                    updatedElements.add(restElements[0].copy(xPercent = 0.08f, yPercent = 0.34f, widthPercent = 0.40f, heightPercent = 0.55f))
                    updatedElements.add(restElements[1].copy(xPercent = 0.52f, yPercent = 0.34f, widthPercent = 0.40f, heightPercent = 0.55f))
                } else if (restElements.size >= 3) {
                    updatedElements.add(restElements[0].copy(xPercent = 0.08f, yPercent = 0.34f, widthPercent = 0.48f, heightPercent = 0.55f))
                    updatedElements.add(restElements[1].copy(xPercent = 0.58f, yPercent = 0.34f, widthPercent = 0.34f, heightPercent = 0.26f))
                    updatedElements.add(restElements[2].copy(xPercent = 0.58f, yPercent = 0.63f, widthPercent = 0.34f, heightPercent = 0.26f))
                    for (i in 3 until restElements.size) {
                        updatedElements.add(restElements[i].copy(xPercent = 0.08f + (i * 0.02f), yPercent = 0.70f, widthPercent = 0.25f, heightPercent = 0.18f))
                    }
                }
            }

            LayoutPreset.ASYMMETRIC_SPLIT -> {
                badgeEl?.let {
                    updatedElements.add(it.copy(xPercent = 0.08f, yPercent = 0.10f, widthPercent = 0.35f, heightPercent = 0.07f))
                }
                updatedElements.add(
                    headingEl.copy(
                        xPercent = 0.08f,
                        yPercent = 0.20f,
                        widthPercent = 0.40f,
                        heightPercent = 0.65f,
                        fontSizeSp = 24f
                    )
                )

                val count = restElements.size
                restElements.forEachIndexed { index, el ->
                    val y = 0.15f + (index * (0.75f / maxOf(1, count)))
                    val h = (0.70f / maxOf(1, count)) - 0.03f
                    updatedElements.add(el.copy(xPercent = 0.52f, yPercent = y, widthPercent = 0.40f, heightPercent = maxOf(0.18f, h)))
                }
            }

            LayoutPreset.EXECUTIVE_METRICS -> {
                badgeEl?.let {
                    updatedElements.add(it.copy(xPercent = 0.08f, yPercent = 0.08f, widthPercent = 0.35f, heightPercent = 0.07f))
                }
                updatedElements.add(
                    headingEl.copy(
                        xPercent = 0.08f,
                        yPercent = if (badgeEl != null) 0.16f else 0.08f,
                        widthPercent = 0.84f,
                        heightPercent = 0.14f,
                        fontSizeSp = 22f
                    )
                )

                val count = maxOf(1, restElements.size)
                val cardWidth = (0.84f / count) - 0.03f
                restElements.forEachIndexed { index, el ->
                    val x = 0.08f + (index * (cardWidth + 0.03f))
                    updatedElements.add(
                        el.copy(
                            xPercent = x,
                            yPercent = 0.36f,
                            widthPercent = cardWidth,
                            heightPercent = 0.52f
                        )
                    )
                }
            }

            LayoutPreset.MINIMAL_CENTER -> {
                badgeEl?.let {
                    updatedElements.add(it.copy(xPercent = 0.30f, yPercent = 0.12f, widthPercent = 0.40f, heightPercent = 0.07f))
                }
                updatedElements.add(
                    headingEl.copy(
                        xPercent = 0.10f,
                        yPercent = 0.24f,
                        widthPercent = 0.80f,
                        heightPercent = 0.25f,
                        fontSizeSp = 26f
                    )
                )

                restElements.forEachIndexed { index, el ->
                    updatedElements.add(
                        el.copy(
                            xPercent = 0.12f,
                            yPercent = 0.52f + (index * 0.22f),
                            widthPercent = 0.76f,
                            heightPercent = 0.20f
                        )
                    )
                }
            }

            LayoutPreset.TIMELINE_FLOW -> {
                badgeEl?.let {
                    updatedElements.add(it.copy(xPercent = 0.08f, yPercent = 0.08f, widthPercent = 0.35f, heightPercent = 0.07f))
                }
                updatedElements.add(
                    headingEl.copy(
                        xPercent = 0.08f,
                        yPercent = if (badgeEl != null) 0.16f else 0.08f,
                        widthPercent = 0.84f,
                        heightPercent = 0.14f,
                        fontSizeSp = 22f
                    )
                )

                val count = maxOf(1, restElements.size)
                val w = (0.84f / count) - 0.03f
                restElements.forEachIndexed { index, el ->
                    val x = 0.08f + (index * (w + 0.03f))
                    updatedElements.add(
                        el.copy(
                            xPercent = x,
                            yPercent = 0.36f,
                            widthPercent = w,
                            heightPercent = 0.52f
                        )
                    )
                }
            }

            LayoutPreset.PROBLEM_SOLUTION -> {
                badgeEl?.let {
                    updatedElements.add(it.copy(xPercent = 0.08f, yPercent = 0.08f, widthPercent = 0.40f, heightPercent = 0.07f))
                }
                updatedElements.add(
                    headingEl.copy(
                        xPercent = 0.08f,
                        yPercent = if (badgeEl != null) 0.16f else 0.08f,
                        widthPercent = 0.84f,
                        heightPercent = 0.14f,
                        fontSizeSp = 22f
                    )
                )

                if (restElements.size >= 2) {
                    updatedElements.add(restElements[0].copy(xPercent = 0.08f, yPercent = 0.36f, widthPercent = 0.40f, heightPercent = 0.54f))
                    updatedElements.add(restElements[1].copy(xPercent = 0.52f, yPercent = 0.36f, widthPercent = 0.40f, heightPercent = 0.54f))
                    for (i in 2 until restElements.size) {
                        updatedElements.add(restElements[i].copy(xPercent = 0.08f + (i * 0.05f), yPercent = 0.72f, widthPercent = 0.25f, heightPercent = 0.18f))
                    }
                } else if (restElements.size == 1) {
                    updatedElements.add(restElements[0].copy(xPercent = 0.08f, yPercent = 0.36f, widthPercent = 0.84f, heightPercent = 0.54f))
                }
            }
        }

        return slide.copy(
            layoutPreset = preset,
            elements = updatedElements,
            aiLayoutRationale = "Re-organized by AI Engine into ${preset.title}: optimized spacing, zero overlaps, and high visual balance."
        )
    }

    fun autoSmartReLayout(slide: Slide): Slide {
        val nextPreset = when (slide.layoutPreset) {
            LayoutPreset.BENTO_GRID -> LayoutPreset.ASYMMETRIC_SPLIT
            LayoutPreset.ASYMMETRIC_SPLIT -> LayoutPreset.EXECUTIVE_METRICS
            LayoutPreset.EXECUTIVE_METRICS -> LayoutPreset.PROBLEM_SOLUTION
            LayoutPreset.PROBLEM_SOLUTION -> LayoutPreset.MINIMAL_CENTER
            LayoutPreset.MINIMAL_CENTER -> LayoutPreset.TIMELINE_FLOW
            LayoutPreset.TIMELINE_FLOW -> LayoutPreset.BENTO_GRID
        }
        return applyLayoutPreset(slide, nextPreset)
    }

    fun polishCopy(original: String, tone: String = "Executive"): String {
        val trimmed = original.trim()
        if (trimmed.isEmpty()) return original

        return when (tone.lowercase()) {
            "punchy" -> trimmed.replace("in order to", "to")
                .replace("a variety of", "key")
                .replace("utilizing", "using")
                .let { if (!it.endsWith(".")) "$it." else it }
            "visionary" -> "Transforming industry paradigms: $trimmed"
            "data-driven" -> "Quantified impact: $trimmed (99.8% precision benchmark)"
            else -> "Strategic mandate: $trimmed"
        }
    }
}
